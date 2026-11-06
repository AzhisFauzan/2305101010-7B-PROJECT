package com.example.belajar3.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.belajar3.data.PlantDatabaseHelper
import com.example.belajar3.model.Plant
import java.util.Calendar

object PlantReminderScheduler {

    const val ACTION_CHECK_CARE = "com.example.belajar3.ACTION_CHECK_PLANT_CARE"
    const val EXTRA_PLANT_ID = "extra_plant_id"

    fun scheduleDailyReminder(context: Context, plant: Plant) {
        if (!plant.isReminderEnabled) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, PlantCareReceiver::class.java).apply {
            action = ACTION_CHECK_CARE
            putExtra(EXTRA_PLANT_ID, plant.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            plant.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = Calendar.getInstance()
        val triggerTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, plant.reminderHour)
            set(Calendar.MINUTE, plant.reminderMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTime.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder(context: Context, plantId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, PlantCareReceiver::class.java).apply {
            action = ACTION_CHECK_CARE
            putExtra(EXTRA_PLANT_ID, plantId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            plantId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun rescheduleAll(context: Context) {
        val dbHelper = PlantDatabaseHelper.getInstance(context)
        val plants = dbHelper.getAllPlants()
        for (plant in plants) {
            if (plant.isReminderEnabled) {
                scheduleDailyReminder(context, plant)
            }
        }
    }
}
