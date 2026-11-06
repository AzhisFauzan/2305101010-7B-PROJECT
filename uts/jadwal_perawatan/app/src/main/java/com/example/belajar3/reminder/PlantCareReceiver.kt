package com.example.belajar3.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.belajar3.data.PlantDatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlantCareReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != PlantReminderScheduler.ACTION_CHECK_CARE) return

        val plantId = intent.getLongExtra(PlantReminderScheduler.EXTRA_PLANT_ID, -1L)
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dbHelper = PlantDatabaseHelper.getInstance(context)

                if (plantId != -1L) {
                    val plant = dbHelper.getPlantById(plantId)
                    if (plant != null && plant.isReminderEnabled) {
                        val currentHst = plant.calculateCurrentHst()
                        val schedules = dbHelper.getSchedulesForPlant(plantId)

                        // Check tasks due today (or overdue tasks not completed)
                        val dueTasks = schedules.filter { !it.isCompleted && it.targetHst == currentHst }
                        val overdueTasks = schedules.filter { !it.isCompleted && it.targetHst < currentHst }

                        if (dueTasks.isNotEmpty()) {
                            val taskTitles = dueTasks.joinToString(", ") { it.title }
                            PlantNotificationHelper.showCareNotification(
                                context = context,
                                notificationId = plant.id.toInt(),
                                plantName = plant.name,
                                currentHst = currentHst,
                                taskCount = dueTasks.size,
                                taskSummary = taskTitles
                            )
                        } else if (overdueTasks.isNotEmpty()) {
                            // Notify about pending overdue task if nothing specifically today
                            val taskTitles = overdueTasks.take(2).joinToString(", ") { "${it.title} (HST ${it.targetHst})" }
                            PlantNotificationHelper.showCareNotification(
                                context = context,
                                notificationId = plant.id.toInt(),
                                plantName = plant.name,
                                currentHst = currentHst,
                                taskCount = overdueTasks.size,
                                taskSummary = "Ada tugas terlewat: $taskTitles"
                            )
                        }

                        // Re-schedule for next day
                        PlantReminderScheduler.scheduleDailyReminder(context, plant)
                    }
                } else {
                    // Check all plants
                    val plants = dbHelper.getAllPlants()
                    for (plant in plants) {
                        if (!plant.isReminderEnabled) continue
                        val currentHst = plant.calculateCurrentHst()
                        val schedules = dbHelper.getSchedulesForPlant(plant.id)
                        val dueTasks = schedules.filter { !it.isCompleted && it.targetHst == currentHst }

                        if (dueTasks.isNotEmpty()) {
                            val taskTitles = dueTasks.joinToString(", ") { it.title }
                            PlantNotificationHelper.showCareNotification(
                                context = context,
                                notificationId = plant.id.toInt(),
                                plantName = plant.name,
                                currentHst = currentHst,
                                taskCount = dueTasks.size,
                                taskSummary = taskTitles
                            )
                        }
                        PlantReminderScheduler.scheduleDailyReminder(context, plant)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
