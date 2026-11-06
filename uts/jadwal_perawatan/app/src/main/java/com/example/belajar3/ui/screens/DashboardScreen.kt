package com.example.belajar3.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.Plant
import com.example.belajar3.model.ScheduleStatus
import com.example.belajar3.ui.PlantCareViewModel
import com.example.belajar3.ui.TaskFilter
import com.example.belajar3.ui.components.PlantCard
import com.example.belajar3.ui.components.TaskCard
import com.example.belajar3.ui.dialogs.AddEditPlantDialog
import com.example.belajar3.ui.dialogs.CompleteTaskDialog
import com.example.belajar3.ui.theme.BorderSubtle
import com.example.belajar3.ui.theme.DarkGreen
import com.example.belajar3.ui.theme.PrimaryGreen
import com.example.belajar3.ui.theme.StatusDone
import com.example.belajar3.ui.theme.StatusToday
import com.example.belajar3.ui.theme.StatusOverdue
import com.example.belajar3.ui.theme.TextPrimary
import com.example.belajar3.ui.theme.TextSecondary
import com.example.belajar3.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PlantCareViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val plants by viewModel.plants.collectAsState()
    val schedules by viewModel.schedules.collectAsState()
    val allTasksWithPlant by viewModel.allTasksWithPlant.collectAsState()
    val currentFilter by viewModel.taskFilter.collectAsState()
    val selectedPlantId by viewModel.selectedPlantId.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // 0 = Jadwal, 1 = Kebun
    var showAddPlantDialog by remember { mutableStateOf(false) }
    var completingSchedule by remember { mutableStateOf<CareSchedule?>(null) }

    val todayFormatted = remember {
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale.forLanguageTag("id-ID")).format(Date())
    }

    // Detail view if a plant is chosen
    val selectedPlant = plants.firstOrNull { it.id == selectedPlantId }
    if (selectedPlant != null) {
        val plantSchedules = schedules.filter { it.plantId == selectedPlant.id }
        PlantDetailScreen(
            plant = selectedPlant,
            schedules = plantSchedules,
            onBack = { viewModel.selectPlant(null) },
            onUpdatePlant = { viewModel.updatePlant(it) },
            onDeletePlant = {
                viewModel.deletePlant(it)
                viewModel.selectPlant(null)
            },
            onAddTask = { viewModel.addCareSchedule(it) },
            onUpdateTask = { viewModel.updateCareSchedule(it) },
            onDeleteTask = { viewModel.deleteCareSchedule(it) },
            onToggleTask = { scheduleId, completed, notes ->
                viewModel.toggleTaskCompletion(scheduleId, completed, notes)
            }
        )
        return
    }

    val todayTasksCount = allTasksWithPlant.count { it.status == ScheduleStatus.TODAY && !it.schedule.isCompleted }
    val overdueTasksCount = allTasksWithPlant.count { it.status == ScheduleStatus.OVERDUE }

    val filteredTasks = allTasksWithPlant.filter { taskWithPlant ->
        when (currentFilter) {
            TaskFilter.TODAY -> taskWithPlant.status == ScheduleStatus.TODAY || taskWithPlant.status == ScheduleStatus.OVERDUE
            TaskFilter.UPCOMING -> taskWithPlant.status == ScheduleStatus.UPCOMING
            TaskFilter.ALL -> true
            TaskFilter.OVERDUE -> taskWithPlant.status == ScheduleStatus.OVERDUE
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Logo",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(28.dp).padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Kebun Pintar",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = todayFormatted,
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.triggerTestNotification()
                            Toast.makeText(context, "Pengingat offline dikirim ke status bar", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsNone,
                            contentDescription = "Uji Pengingat",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddPlantDialog = true },
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Tanaman",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            // Minimalist 2-Segment Switch (Jadwal & Kebun)
            Spacer(modifier = Modifier.height(16.dp))
            SegmentedSwitch(
                selectedIndex = activeTab,
                onSelected = { activeTab = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (activeTab == 0) {
                // TAB 0: JADWAL
                // Minimal Status Sentence
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val statusText = when {
                        todayTasksCount > 0 -> "$todayTasksCount tugas perawatan perlu dikerjakan hari ini"
                        overdueTasksCount > 0 -> "$overdueTasksCount tugas terlewat belum diselesaikan"
                        else -> "Semua tugas hari ini telah selesai"
                    }
                    val statusColor = when {
                        todayTasksCount > 0 -> StatusToday
                        overdueTasksCount > 0 -> StatusOverdue
                        else -> StatusDone
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(statusColor, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = statusText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clean Minimal Pill Filter
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MinimalFilterPill(
                        title = "Hari Ini",
                        count = todayTasksCount + overdueTasksCount,
                        isSelected = currentFilter == TaskFilter.TODAY,
                        onClick = { viewModel.setFilter(TaskFilter.TODAY) }
                    )
                    MinimalFilterPill(
                        title = "Mendatang",
                        count = allTasksWithPlant.count { it.status == ScheduleStatus.UPCOMING },
                        isSelected = currentFilter == TaskFilter.UPCOMING,
                        onClick = { viewModel.setFilter(TaskFilter.UPCOMING) }
                    )
                    MinimalFilterPill(
                        title = "Semua",
                        count = allTasksWithPlant.size,
                        isSelected = currentFilter == TaskFilter.ALL,
                        onClick = { viewModel.setFilter(TaskFilter.ALL) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Task List
                if (filteredTasks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada tugas pada daftar ini",
                            fontSize = 14.sp,
                            color = TextTertiary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTasks, key = { it.schedule.id }) { item ->
                            TaskCard(
                                schedule = item.schedule,
                                plant = item.plant,
                                status = item.status,
                                onToggleCompletion = { checked ->
                                    if (checked) {
                                        completingSchedule = item.schedule
                                    } else {
                                        viewModel.toggleTaskCompletion(item.schedule.id, false, "")
                                    }
                                },
                                onClick = {
                                    viewModel.selectPlant(item.plant.id)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            } else {
                // TAB 1: KEBUN SAYA (DAFTAR TANAMAN)
                if (plants.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Kebun masih kosong",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Ketuk tombol + di bawah untuk menambah tanaman",
                                fontSize = 13.sp,
                                color = TextTertiary
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(plants, key = { it.id }) { plant ->
                            val plantSchedules = schedules.filter { it.plantId == plant.id }
                            val completedCount = plantSchedules.count { it.isCompleted }
                            PlantCard(
                                plant = plant,
                                completedTasksCount = completedCount,
                                totalTasksCount = plantSchedules.size,
                                onClick = { viewModel.selectPlant(plant.id) }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }

    if (showAddPlantDialog) {
        AddEditPlantDialog(
            onDismiss = { showAddPlantDialog = false },
            onConfirm = { name, variety, plantingDateMillis, plotLocation, targetHarvestHst, reminderHour, reminderMinute, isReminderEnabled, notes, template ->
                viewModel.addPlant(
                    name = name,
                    variety = variety,
                    plantingDateMillis = plantingDateMillis,
                    plotLocation = plotLocation,
                    targetHarvestHst = targetHarvestHst,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute,
                    isReminderEnabled = isReminderEnabled,
                    notes = notes,
                    template = template
                )
                showAddPlantDialog = false
            }
        )
    }

    completingSchedule?.let { schedule ->
        CompleteTaskDialog(
            schedule = schedule,
            onDismiss = { completingSchedule = null },
            onConfirm = { notes ->
                viewModel.toggleTaskCompletion(schedule.id, true, notes)
                completingSchedule = null
            }
        )
    }
}

@Composable
private fun SegmentedSwitch(
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFE8F5E9) // LightGreen
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            val options = listOf("Jadwal Perawatan", "Kebun Saya")
            options.forEachIndexed { index, label ->
                val isSelected = selectedIndex == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) PrimaryGreen else Color.Transparent)
                        .clickable { onSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else DarkGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun MinimalFilterPill(
    title: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) PrimaryGreen else Color.White,
        border = BorderStroke(1.dp, if (isSelected) PrimaryGreen else BorderSubtle),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = "$title ($count)",
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) Color.White else TextPrimary,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}
