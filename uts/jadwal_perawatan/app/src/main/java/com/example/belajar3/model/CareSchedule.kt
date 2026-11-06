package com.example.belajar3.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ScheduleStatus(val displayName: String) {
    COMPLETED("Selesai"),
    TODAY("Hari Ini"),
    OVERDUE("Terlewat"),
    UPCOMING("Mendatang")
}

data class CareSchedule(
    val id: Long = 0,
    val plantId: Long,
    val targetHst: Int,
    val careType: CareType,
    val title: String,
    val description: String = "",
    val isCompleted: Boolean = false,
    val completedDateMillis: Long? = null,
    val completionNotes: String = ""
) {
    fun getStatus(currentHst: Int): ScheduleStatus {
        return when {
            isCompleted -> ScheduleStatus.COMPLETED
            targetHst == currentHst -> ScheduleStatus.TODAY
            targetHst < currentHst -> ScheduleStatus.OVERDUE
            else -> ScheduleStatus.UPCOMING
        }
    }

    fun formatTargetDate(plantingDateMillis: Long): String {
        val dateMillis = Plant.normalizeToMidnight(plantingDateMillis) + (targetHst.toLong() * 86_400_000L)
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy", Locale.forLanguageTag("id-ID"))
        return sdf.format(Date(dateMillis))
    }

    fun formatCompletedDate(): String? {
        val completed = completedDateMillis ?: return null
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID"))
        return sdf.format(Date(completed))
    }
}
