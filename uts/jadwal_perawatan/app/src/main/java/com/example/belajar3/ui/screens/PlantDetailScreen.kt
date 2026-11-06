package com.example.belajar3.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.Plant
import com.example.belajar3.model.ScheduleStatus
import com.example.belajar3.ui.dialogs.AddEditPlantDialog
import com.example.belajar3.ui.dialogs.AddEditTaskDialog
import com.example.belajar3.ui.dialogs.CompleteTaskDialog
import com.example.belajar3.ui.theme.BorderSubtle
import com.example.belajar3.ui.theme.Destructive
import com.example.belajar3.ui.theme.LightGreen
import com.example.belajar3.ui.theme.PrimaryGreen
import com.example.belajar3.ui.theme.StatusDone
import com.example.belajar3.ui.theme.StatusOverdue
import com.example.belajar3.ui.theme.StatusToday
import com.example.belajar3.ui.theme.TextPrimary
import com.example.belajar3.ui.theme.TextSecondary
import com.example.belajar3.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    plant: Plant,
    schedules: List<CareSchedule>,
    onBack: () -> Unit,
    onUpdatePlant: (Plant) -> Unit,
    onDeletePlant: (Long) -> Unit,
    onAddTask: (CareSchedule) -> Unit,
    onUpdateTask: (CareSchedule) -> Unit,
    onDeleteTask: (Long) -> Unit,
    onToggleTask: (scheduleId: Long, completed: Boolean, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditPlantDialog by remember { mutableStateOf(false) }
    var showDeletePlantConfirm by remember { mutableStateOf(false) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var editingSchedule by remember { mutableStateOf<CareSchedule?>(null) }
    var completingSchedule by remember { mutableStateOf<CareSchedule?>(null) }

    val currentHst = plant.calculateCurrentHst()
    val progress = plant.getHarvestProgress()
    val completedCount = schedules.count { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = plant.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showEditPlantDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { showDeletePlantConfirm = true }) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Hapus",
                            tint = Destructive,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true },
                containerColor = PrimaryGreen,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Tugas", modifier = Modifier.size(24.dp))
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Clean Minimal Hero Card
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, BorderSubtle),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = if (currentHst >= 0) "HST $currentHst" else "H$currentHst (Pra-Tanam)",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrimaryGreen
                                )
                                Text(
                                    text = plant.getGrowthStage(),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextSecondary
                                )
                            }

                            Text(
                                text = "$completedCount/${schedules.size} selesai",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Progress Bar
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = PrimaryGreen,
                            trackColor = Color(0xFFE8ECE8)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Planting & Harvest info in single subtle line
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Tanam: ${plant.formatPlantingDate()}",
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                            Text(
                                text = "Target Panen: ${plant.targetHarvestHst} HST",
                                fontSize = 12.sp,
                                color = TextTertiary
                            )
                        }
                    }
                }
            }

            // Section Label
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jadwal Berdasarkan HST",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${schedules.size} Jadwal",
                        fontSize = 12.sp,
                        color = TextTertiary
                    )
                }
            }

            if (schedules.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada jadwal. Ketuk + untuk menambahkan.",
                            fontSize = 13.sp,
                            color = TextTertiary
                        )
                    }
                }
            } else {
                items(schedules, key = { it.id }) { schedule ->
                    val status = schedule.getStatus(currentHst)
                    val isDone = schedule.isCompleted

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDone) LightGreen else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, BorderSubtle),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Checkbox
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(if (isDone) StatusDone else Color.Transparent)
                                    .border(
                                        width = 1.5.dp,
                                        color = if (isDone) StatusDone else Color(0xFFB0B7B0),
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        if (!isDone) {
                                            completingSchedule = schedule
                                        } else {
                                            onToggleTask(schedule.id, false, "")
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = schedule.title,
                                    fontSize = 15.sp,
                                    fontWeight = if (isDone) FontWeight.Normal else FontWeight.SemiBold,
                                    color = if (isDone) TextSecondary else TextPrimary,
                                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "HST ${schedule.targetHst}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDone) TextTertiary else PrimaryGreen
                                    )
                                    Text(
                                        text = " • ${schedule.formatTargetDate(plant.plantingDateMillis)}",
                                        fontSize = 11.sp,
                                        color = TextTertiary
                                    )
                                    if (!isDone && status == ScheduleStatus.TODAY) {
                                        Text(
                                            text = " • Hari Ini",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusToday
                                        )
                                    } else if (!isDone && status == ScheduleStatus.OVERDUE) {
                                        Text(
                                            text = " • Terlewat",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusOverdue
                                        )
                                    }
                                }

                                if (schedule.description.isNotBlank() && !isDone) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = schedule.description,
                                        fontSize = 12.sp,
                                        color = TextSecondary,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            // Minimal Edit & Delete
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { editingSchedule = schedule },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteTask(schedule.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Hapus",
                                        tint = Destructive,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showEditPlantDialog) {
        AddEditPlantDialog(
            initialPlant = plant,
            onDismiss = { showEditPlantDialog = false },
            onConfirm = { name, variety, plantingDateMillis, plotLocation, targetHarvestHst, reminderHour, reminderMinute, isReminderEnabled, notes, _ ->
                onUpdatePlant(
                    plant.copy(
                        name = name,
                        variety = variety,
                        plantingDateMillis = plantingDateMillis,
                        plotLocation = plotLocation,
                        targetHarvestHst = targetHarvestHst,
                        reminderHour = reminderHour,
                        reminderMinute = reminderMinute,
                        isReminderEnabled = isReminderEnabled,
                        notes = notes
                    )
                )
                showEditPlantDialog = false
            }
        )
    }

    if (showDeletePlantConfirm) {
        AlertDialog(
            onDismissRequest = { showDeletePlantConfirm = false },
            title = { Text("Hapus Tanaman?", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 20.sp) },
            text = { Text("Hapus '${plant.name}' dan seluruh jadwal perawatannya?", color = TextSecondary, fontSize = 15.sp) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeletePlant(plant.id)
                        showDeletePlantConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Destructive),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Hapus", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeletePlantConfirm = false }) {
                    Text("Batal", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showAddTaskDialog) {
        AddEditTaskDialog(
            plant = plant,
            onDismiss = { showAddTaskDialog = false },
            onConfirm = { targetHst, careType, title, description ->
                onAddTask(
                    CareSchedule(
                        plantId = plant.id,
                        targetHst = targetHst,
                        careType = careType,
                        title = title,
                        description = description
                    )
                )
                showAddTaskDialog = false
            }
        )
    }

    editingSchedule?.let { schedule ->
        AddEditTaskDialog(
            plant = plant,
            initialSchedule = schedule,
            onDismiss = { editingSchedule = null },
            onConfirm = { targetHst, careType, title, description ->
                onUpdateTask(
                    schedule.copy(
                        targetHst = targetHst,
                        careType = careType,
                        title = title,
                        description = description
                    )
                )
                editingSchedule = null
            }
        )
    }

    completingSchedule?.let { schedule ->
        CompleteTaskDialog(
            schedule = schedule,
            onDismiss = { completingSchedule = null },
            onConfirm = { notes ->
                onToggleTask(schedule.id, true, notes)
                completingSchedule = null
            }
        )
    }
}
