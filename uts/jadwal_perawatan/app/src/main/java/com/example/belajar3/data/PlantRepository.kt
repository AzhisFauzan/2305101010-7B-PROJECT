package com.example.belajar3.data

import android.content.Context
import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.Plant
import com.example.belajar3.model.PlantTemplate
import com.example.belajar3.model.TemplateTask
import com.example.belajar3.reminder.PlantReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantRepository(private val context: Context) {

    private val dbHelper = PlantDatabaseHelper.getInstance(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _plants = MutableStateFlow<List<Plant>>(emptyList())
    val plants: StateFlow<List<Plant>> = _plants.asStateFlow()

    private val _schedules = MutableStateFlow<List<CareSchedule>>(emptyList())
    val schedules: StateFlow<List<CareSchedule>> = _schedules.asStateFlow()

    init {
        scope.launch {
            loadInitialData()
        }
    }

    private suspend fun loadInitialData() = withContext(Dispatchers.IO) {
        val existingPlants = dbHelper.getAllPlants()
        if (existingPlants.isEmpty()) {
            // Seed a starter plant (Cabai Rawit) so user immediately gets a realistic experience
            seedSamplePlant()
        }
        refreshData()
    }

    private suspend fun seedSamplePlant() {
        val now = System.currentTimeMillis()
        // Planting date set to 14 days ago so current HST is 14
        val plantingDate14DaysAgo = Plant.normalizeToMidnight(now) - (14L * 86_400_000L)

        val samplePlant = Plant(
            name = "Cabai Rawit Merah",
            variety = "Ori 212",
            plantingDateMillis = plantingDate14DaysAgo,
            plotLocation = "Bedengan Utama #1",
            targetHarvestHst = 85,
            reminderHour = 7,
            reminderMinute = 0,
            isReminderEnabled = true,
            notes = "Bedengan mulsa hitam perak, jarak tanam 50x60 cm. Dipupuk dasar kompos kambing."
        )

        val chiliTemplate = PlantTemplate.getAllTemplates().firstOrNull { it.id == "cabai" }
        val templateTasks = chiliTemplate?.tasks ?: emptyList()

        val plantId = dbHelper.insertPlant(samplePlant)
        val sampleSchedules = templateTasks.map { task ->
            val isAlreadyDone = task.targetHst < 14
            CareSchedule(
                plantId = plantId,
                targetHst = task.targetHst,
                careType = task.careType,
                title = task.title,
                description = task.description,
                isCompleted = isAlreadyDone,
                completedDateMillis = if (isAlreadyDone) plantingDate14DaysAgo + (task.targetHst.toLong() * 86_400_000L) else null,
                completionNotes = if (isAlreadyDone) "Selesai dikerjakan sesuai dosis rekomendasi." else ""
            )
        }
        dbHelper.insertCareSchedules(sampleSchedules)
        PlantReminderScheduler.scheduleDailyReminder(context, samplePlant.copy(id = plantId))
    }

    suspend fun refreshData() = withContext(Dispatchers.IO) {
        _plants.value = dbHelper.getAllPlants()
        _schedules.value = dbHelper.getAllSchedules()
    }

    suspend fun addPlant(plant: Plant, templateTasks: List<TemplateTask>? = null): Long = withContext(Dispatchers.IO) {
        val plantId = dbHelper.insertPlant(plant)
        val createdPlant = plant.copy(id = plantId)

        if (!templateTasks.isNullOrEmpty()) {
            val schedulesToInsert = templateTasks.map { task ->
                CareSchedule(
                    plantId = plantId,
                    targetHst = task.targetHst,
                    careType = task.careType,
                    title = task.title,
                    description = task.description,
                    isCompleted = false
                )
            }
            dbHelper.insertCareSchedules(schedulesToInsert)
        }

        if (createdPlant.isReminderEnabled) {
            PlantReminderScheduler.scheduleDailyReminder(context, createdPlant)
        }

        refreshData()
        plantId
    }

    suspend fun updatePlant(plant: Plant) = withContext(Dispatchers.IO) {
        dbHelper.updatePlant(plant)
        if (plant.isReminderEnabled) {
            PlantReminderScheduler.scheduleDailyReminder(context, plant)
        } else {
            PlantReminderScheduler.cancelReminder(context, plant.id)
        }
        refreshData()
    }

    suspend fun deletePlant(plantId: Long) = withContext(Dispatchers.IO) {
        PlantReminderScheduler.cancelReminder(context, plantId)
        dbHelper.deletePlant(plantId)
        refreshData()
    }

    suspend fun addCareSchedule(schedule: CareSchedule): Long = withContext(Dispatchers.IO) {
        val id = dbHelper.insertCareSchedule(schedule)
        refreshData()
        id
    }

    suspend fun updateCareSchedule(schedule: CareSchedule) = withContext(Dispatchers.IO) {
        dbHelper.updateCareSchedule(schedule)
        refreshData()
    }

    suspend fun deleteCareSchedule(scheduleId: Long) = withContext(Dispatchers.IO) {
        dbHelper.deleteCareSchedule(scheduleId)
        refreshData()
    }

    suspend fun toggleTaskCompletion(scheduleId: Long, completed: Boolean, notes: String = "") = withContext(Dispatchers.IO) {
        val completionTime = if (completed) System.currentTimeMillis() else null
        dbHelper.setScheduleCompletion(scheduleId, completed, completionTime, notes)
        refreshData()
    }

    fun getSchedulesForPlant(plantId: Long): List<CareSchedule> {
        return _schedules.value.filter { it.plantId == plantId }
    }

    companion object {
        @Volatile
        private var instance: PlantRepository? = null

        fun getInstance(context: Context): PlantRepository {
            return instance ?: synchronized(this) {
                instance ?: PlantRepository(context.applicationContext).also { instance = it }
            }
        }
    }
}
