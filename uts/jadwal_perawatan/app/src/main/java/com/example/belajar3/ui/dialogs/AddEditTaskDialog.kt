package com.example.belajar3.ui.dialogs

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.CareType
import com.example.belajar3.model.Plant
import com.example.belajar3.ui.theme.BorderSubtle
import com.example.belajar3.ui.theme.PrimaryGreen
import com.example.belajar3.ui.theme.TextPrimary
import com.example.belajar3.ui.theme.TextSecondary
import com.example.belajar3.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditTaskDialog(
    plant: Plant,
    initialSchedule: CareSchedule? = null,
    onDismiss: () -> Unit,
    onConfirm: (
        targetHst: Int,
        careType: CareType,
        title: String,
        description: String
    ) -> Unit
) {
    val isEditMode = initialSchedule != null

    var targetHst by remember { mutableIntStateOf(initialSchedule?.targetHst ?: plant.calculateCurrentHst().coerceAtLeast(0)) }
    var selectedCareType by remember { mutableStateOf(initialSchedule?.careType ?: CareType.FERTILIZING) }
    var title by remember { mutableStateOf(initialSchedule?.title ?: "") }
    var description by remember { mutableStateOf(initialSchedule?.description ?: "") }

    val dateFormat = remember { SimpleDateFormat("EEEE, d MMMM yyyy", Locale.forLanguageTag("id-ID")) }

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
                // Title
                Text(
                    text = if (isEditMode) "Edit Jadwal" else "Tambah Jadwal HST",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
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

                    // Target HST input
                    OutlinedTextField(
                        value = targetHst.toString(),
                        onValueChange = { targetHst = it.toIntOrNull() ?: 0 },
                        label = { Text("Target HST") },
                        placeholder = { Text("Contoh: 14") },
                        singleLine = true,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Computed Date Preview
                    val computedDateMillis = plant.getDateForHst(targetHst)
                    Surface(
                        color = Color(0xFFE8F5E9), // LightGreen
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tanggal jatuh tempo:\n${dateFormat.format(Date(computedDateMillis))}",
                            fontSize = 13.sp,
                            color = PrimaryGreen,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp),
                            lineHeight = 18.sp
                        )
                    }

                    // Jenis Tindakan
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Jenis Tindakan",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (type in CareType.values()) {
                                val isSelected = selectedCareType == type
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) PrimaryGreen else Color.White,
                                    border = BorderStroke(1.dp, if (isSelected) PrimaryGreen else BorderSubtle),
                                    modifier = Modifier.clickable { selectedCareType = type }
                                ) {
                                    Text(
                                        text = type.displayName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else TextPrimary,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Judul
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Nama Tindakan / Tugas") },
                        placeholder = { Text("Contoh: Cor NPK 16-16-16") },
                        singleLine = true,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Dosis / Keterangan
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Dosis / Petunjuk (Opsional)") },
                        placeholder = { Text("Contoh: 5 gram per tanaman") },
                        minLines = 3,
                        maxLines = 5,
                        colors = tfColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
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
                            if (title.isNotBlank()) {
                                onConfirm(targetHst, selectedCareType, title, description)
                            }
                        },
                        enabled = title.isNotBlank(),
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
