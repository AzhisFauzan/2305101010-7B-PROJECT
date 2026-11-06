package com.example.belajar3.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.belajar3.data.PlantRepository
import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.Plant
import com.example.belajar3.model.PlantTemplate
import com.example.belajar3.model.ScheduleStatus
import com.example.belajar3.model.TemplateTask
import com.example.belajar3.reminder.PlantNotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TaskWithPlant(
    val schedule: CareSchedule,
    val plant: Plant,
    val status: ScheduleStatus
)

enum class TaskFilter(val title: String) {
    TODAY("Hari Ini"),
    OVERDUE("Terlewat"),
    UPCOMING("Mendatang"),
    ALL("Semua")
}

class PlantCareViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PlantRepository.getInstance(application)

    val plants: StateFlow<List<Plant>> = repository.plants
    val schedules: StateFlow<List<CareSchedule>> = repository.schedules

    private val _selectedPlantId = MutableStateFlow<Long?>(null)
    val selectedPlantId: StateFlow<Long?> = _selectedPlantId.asStateFlow()

    private val _taskFilter = MutableStateFlow(TaskFilter.TODAY)
    val taskFilter: StateFlow<TaskFilter> = _taskFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Combined list of tasks with plant info
    val allTasksWithPlant: StateFlow<List<TaskWithPlant>> = combine(
        plants,
        schedules,
        _searchQuery
    ) { plantList, scheduleList, query ->
        val plantMap = plantList.associateBy { it.id }
        scheduleList.mapNotNull { schedule ->
            val plant = plantMap[schedule.plantId] ?: return@mapNotNull null
            val currentHst = plant.calculateCurrentHst()
            val status = schedule.getStatus(currentHst)
            TaskWithPlant(schedule = schedule, plant = plant, status = status)
        }.filter { taskWithPlant ->
            if (query.isBlank()) true
            else {
                taskWithPlant.schedule.title.contains(query, ignoreCase = true) ||
                        taskWithPlant.plant.name.contains(query, ignoreCase = true) ||
                        taskWithPlant.schedule.description.contains(query, ignoreCase = true)
            }
        }.sortedWith(
            compareBy<TaskWithPlant> { it.schedule.isCompleted }
                .thenBy { it.schedule.targetHst }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: TaskFilter) {
        _taskFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPlant(plantId: Long?) {
        _selectedPlantId.value = plantId
    }

    fun addPlant(
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
    ) {
        viewModelScope.launch {
            val plant = Plant(
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
            repository.addPlant(plant, template?.tasks)
        }
    }

    fun updatePlant(plant: Plant) {
        viewModelScope.launch {
            repository.updatePlant(plant)
        }
    }

    fun deletePlant(plantId: Long) {
        viewModelScope.launch {
            if (_selectedPlantId.value == plantId) {
                _selectedPlantId.value = null
            }
            repository.deletePlant(plantId)
        }
    }

    fun addCareSchedule(schedule: CareSchedule) {
        viewModelScope.launch {
            repository.addCareSchedule(schedule)
        }
    }

    fun updateCareSchedule(schedule: CareSchedule) {
        viewModelScope.launch {
            repository.updateCareSchedule(schedule)
        }
    }

    fun deleteCareSchedule(scheduleId: Long) {
        viewModelScope.launch {
            repository.deleteCareSchedule(scheduleId)
        }
    }

    fun toggleTaskCompletion(scheduleId: Long, completed: Boolean, notes: String = "") {
        viewModelScope.launch {
            repository.toggleTaskCompletion(scheduleId, completed, notes)
        }
    }

    fun triggerTestNotification() {
        PlantNotificationHelper.showTestNotification(getApplication())
    }
}
