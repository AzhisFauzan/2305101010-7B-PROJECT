package com.example.belajar3.ui.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belajar3.model.Plant
import com.example.belajar3.model.PlantTemplate
import com.example.belajar3.ui.theme.BorderSubtle
import com.example.belajar3.ui.theme.PrimaryGreen
import com.example.belajar3.ui.theme.TextPrimary
import com.example.belajar3.ui.theme.TextSecondary
import com.example.belajar3.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditPlantDialog(
    initialPlant: Plant? = null,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        variety: String,
        plantingDateMillis: Long,
        plotLocation: String,
        targetHarvestHst: Int,
        reminderHour: Int,
        reminderMinute: Int,
        isReminderEnabled: Boolean,
        notes: String,
        template: PlantTemplate?
    ) -> Unit
) {
    val context = LocalContext.current
    val isEditMode = initialPlant != null

    val templates = remember { PlantTemplate.getAllTemplates() }
    var selectedTemplate by remember { mutableStateOf<PlantTemplate?>(if (isEditMode) null else templates.first()) }

    var name by remember { mutableStateOf(initialPlant?.name ?: selectedTemplate?.name ?: "") }
    var variety by remember { mutableStateOf(initialPlant?.variety ?: selectedTemplate?.defaultVariety ?: "") }
    var plotLocation by remember { mutableStateOf(initialPlant?.plotLocation ?: "") }
    var targetHarvestHst by remember { mutableIntStateOf(initialPlant?.targetHarvestHst ?: selectedTemplate?.estimatedHarvestHst ?: 90) }
    var plantingDateMillis by remember { mutableLongStateOf(initialPlant?.plantingDateMillis ?: Plant.normalizeToMidnight(System.currentTimeMillis())) }
    var reminderHour by remember { mutableIntStateOf(initialPlant?.reminderHour ?: 7) }
    var reminderMinute by remember { mutableIntStateOf(initialPlant?.reminderMinute ?: 0) }
    var isReminderEnabled by remember { mutableStateOf(initialPlant?.isReminderEnabled ?: true) }

    val dateFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID")) }

    val tfColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PrimaryGreen,
        unfocusedBorderColor = BorderSubtle,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedLabelColor = PrimaryGreen,
        unfocusedLabelColor = TextSecondary,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = if (isEditMode) "Edit Tanaman" else "Tambah Tanaman",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Template Selector Chips (Only when adding)
                    if (!isEditMode) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Pilih Jenis Tanaman",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextSecondary
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (tmpl in templates) {
                                    val isSelected = selectedTemplate?.id == tmpl.id
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) PrimaryGreen else Color.White,
                                        border = BorderStroke(1.dp, if (isSelected) PrimaryGreen else BorderSubtle),
                                        modifier = Modifier.clickable {
                                            selectedTemplate = tmpl
                                            name = tmpl.name
                                            variety = tmpl.defaultVariety
                                            targetHarvestHst = tmpl.estimatedHarvestHst
                                        }
                                    ) {
                                        Text(
                                            text = tmpl.name,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else TextPrimary,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Plant Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama Tanaman") },
                        singleLine = true,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Planting Date Picker (Clean row)
                    Column {
                        Text(
                            text = "Tanggal Tanam",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, BorderSubtle),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val cal = Calendar.getInstance().apply { timeInMillis = plantingDateMillis }
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            val picked = Calendar.getInstance().apply {
                                                set(year, month, dayOfMonth, 0, 0, 0)
                                                set(Calendar.MILLISECOND, 0)
                                            }
                                            plantingDateMillis = picked.timeInMillis
                                        },
                                        cal.get(Calendar.YEAR),
                                        cal.get(Calendar.MONTH),
                                        cal.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val calculatedHst = Plant(
                                    name = "",
                                    plantingDateMillis = plantingDateMillis
                                ).calculateCurrentHst()

                                Column {
                                    Text(
                                        text = dateFormat.format(Date(plantingDateMillis)),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (calculatedHst >= 0) "Hari ini: HST $calculatedHst" else "Mulai tanam dalam ${-calculatedHst} hari",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = PrimaryGreen
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Location / Plot (Optional)
                    OutlinedTextField(
                        value = plotLocation,
                        onValueChange = { plotLocation = it },
                        label = { Text("Lokasi / Lahan (Opsional)") },
                        placeholder = { Text("Contoh: Bedengan 1") },
                        singleLine = true,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Minimal Reminder Toggle & Time
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF7FAF7), // SoftBackground
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable(enabled = isReminderEnabled) {
                                        TimePickerDialog(
                                            context,
                                            { _, hourOfDay, minute ->
                                                reminderHour = hourOfDay
                                                reminderMinute = minute
                                            },
                                            reminderHour,
                                            reminderMinute,
                                            true
                                        ).show()
                                    }
                            ) {
                                Text(
                                    text = "Pengingat Harian",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isReminderEnabled)
                                        String.format(Locale.getDefault(), "%02d:%02d WIB (Ketuk untuk ubah)", reminderHour, reminderMinute)
                                    else "Dinonaktifkan",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isReminderEnabled) PrimaryGreen else TextTertiary
                                )
                            }

                            Switch(
                                checked = isReminderEnabled,
                                onCheckedChange = { isReminderEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = PrimaryGreen,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Color(0xFFD8E2D8),
                                    uncheckedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = TextSecondary, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(
                                    name,
                                    variety,
                                    plantingDateMillis,
                                    plotLocation,
                                    targetHarvestHst,
                                    reminderHour,
                                    reminderMinute,
                                    isReminderEnabled,
                                    "",
                                    if (isEditMode) null else selectedTemplate
                                )
                            }
                        },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(if (isEditMode) "Simpan" else "Tambahkan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
