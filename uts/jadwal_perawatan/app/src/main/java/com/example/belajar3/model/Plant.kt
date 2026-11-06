package com.example.belajar3.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class Plant(
    val id: Long = 0,
    val name: String,
    val variety: String = "",
    val plantingDateMillis: Long,
    val plotLocation: String = "",
    val targetHarvestHst: Int = 90,
    val reminderHour: Int = 7,
    val reminderMinute: Int = 0,
    val isReminderEnabled: Boolean = true,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Menghitung HST (Hari Setelah Tanam) saat ini.
     * Hari tanam = HST 0.
     * 1 hari setelah tanam = HST 1, dst.
     */
    fun calculateCurrentHst(nowMillis: Long = System.currentTimeMillis()): Int {
        val plantingMidnight = normalizeToMidnight(plantingDateMillis)
        val todayMidnight = normalizeToMidnight(nowMillis)
        val diffMillis = todayMidnight - plantingMidnight
        return TimeUnit.MILLISECONDS.toDays(diffMillis).toInt()
    }

    fun getHstDisplay(nowMillis: Long = System.currentTimeMillis()): String {
        val hst = calculateCurrentHst(nowMillis)
        return when {
            hst < 0 -> "H${hst} (Pra-Tanam)"
            hst == 0 -> "HST 0 (Hari Tanam)"
            else -> "HST $hst"
        }
    }

    fun getGrowthStage(nowMillis: Long = System.currentTimeMillis()): String {
        val hst = calculateCurrentHst(nowMillis)
        val harvest = if (targetHarvestHst > 0) targetHarvestHst else 90

        return when {
            hst < 0 -> "Persiapan Lahan / Semai"
            hst <= (harvest * 0.15).toInt() -> "Vegetatif Awal (Adaptasi)"
            hst <= (harvest * 0.45).toInt() -> "Vegetatif Aktif (Cabang & Daun)"
            hst <= (harvest * 0.70).toInt() -> "Generatif (Pembungaan)"
            hst < harvest -> "Pematangan Buah / Bulir"
            else -> "Fase Panen"
        }
    }

    fun getHarvestProgress(nowMillis: Long = System.currentTimeMillis()): Float {
        val hst = calculateCurrentHst(nowMillis)
        if (targetHarvestHst <= 0) return 0f
        if (hst <= 0) return 0f
        return (hst.toFloat() / targetHarvestHst.toFloat()).coerceIn(0f, 1f)
    }

    fun getEstimatedHarvestDateMillis(): Long {
        return plantingDateMillis + (targetHarvestHst.toLong() * 86_400_000L)
    }

    fun getDateForHst(targetHst: Int): Long {
        return normalizeToMidnight(plantingDateMillis) + (targetHst.toLong() * 86_400_000L)
    }

    fun formatPlantingDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
        return sdf.format(Date(plantingDateMillis))
    }

    fun formatEstimatedHarvestDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID"))
        return sdf.format(Date(getEstimatedHarvestDateMillis()))
    }

    fun formatReminderTime(): String {
        return String.format(Locale.getDefault(), "%02d:%02d", reminderHour, reminderMinute)
    }

    companion object {
        fun normalizeToMidnight(timeMillis: Long): Long {
            val cal = Calendar.getInstance().apply {
                timeInMillis = timeMillis
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            return cal.timeInMillis
        }
    }
}
