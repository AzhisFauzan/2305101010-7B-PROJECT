package com.example.belajar3.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.belajar3.ui.theme.BorderSubtle
import com.example.belajar3.ui.theme.PrimaryGreen
import com.example.belajar3.ui.theme.StatusDone
import com.example.belajar3.ui.theme.StatusOverdue
import com.example.belajar3.ui.theme.DarkGreen
import com.example.belajar3.ui.theme.LightGreen
import com.example.belajar3.ui.theme.StatusToday
import com.example.belajar3.ui.theme.TextPrimary
import com.example.belajar3.ui.theme.TextSecondary
import com.example.belajar3.ui.theme.TextTertiary

@Composable
fun TaskCard(
    schedule: CareSchedule,
    plant: Plant,
    status: ScheduleStatus,
    onToggleCompletion: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDone = schedule.isCompleted

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDone) LightGreen else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Minimal Circular Checkbox
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isDone) StatusDone else Color.Transparent)
                    .border(
                        width = 1.5.dp,
                        color = if (isDone) StatusDone else Color(0xFFB0B7B0),
                        shape = CircleShape
                    )
                    .clickable { onToggleCompletion(!isDone) },
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selesai",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Main Content
            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = schedule.title,
                    fontSize = 15.sp,
                    fontWeight = if (isDone) FontWeight.Normal else FontWeight.SemiBold,
                    color = if (isDone) TextSecondary else DarkGreen,
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Subtitle: Plant Name • HST Target • Status/Date
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plant.name,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " • HST ${schedule.targetHst}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    if (!isDone) {
                        when (status) {
                            ScheduleStatus.TODAY -> {
                                Text(
                                    text = " • Hari Ini",
                                    fontSize = 12.sp,
                                    color = StatusToday,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            ScheduleStatus.OVERDUE -> {
                                Text(
                                    text = " • Terlewat",
                                    fontSize = 12.sp,
                                    color = StatusOverdue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            else -> {
                                Text(
                                    text = " • ${schedule.formatTargetDate(plant.plantingDateMillis)}",
                                    fontSize = 11.sp,
                                    color = TextTertiary
                                )
                            }
                        }
                    }
                }

                if (schedule.description.isNotBlank() && !isDone) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = schedule.description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun PlantCard(
    plant: Plant,
    completedTasksCount: Int,
    totalTasksCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentHst = plant.calculateCurrentHst()
    val progress = plant.getHarvestProgress()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, BorderSubtle),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Plant Information
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = plant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(2.dp))

                val subtitle = buildString {
                    if (plant.variety.isNotBlank()) append(plant.variety)
                    if (plant.plotLocation.isNotBlank()) {
                        if (isNotEmpty()) append(" • ")
                        append(plant.plotLocation)
                    }
                    if (isEmpty()) append(plant.formatPlantingDate())
                }

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Slim Progress
                Row(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = PrimaryGreen,
                        trackColor = Color(0xFFE8ECE8)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$completedTasksCount/$totalTasksCount tugas",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Right: Big Clean HST Number & Indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = if (currentHst >= 0) "$currentHst" else "H$currentHst",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryGreen,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "HST",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextTertiary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
