package com.example.belajar3

import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.CareType
import com.example.belajar3.model.Plant
import com.example.belajar3.model.PlantTemplate
import com.example.belajar3.model.ScheduleStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class PlantHstCalculationTest {

    @Test
    fun testPlantHstCalculation_SameDay_IsHst0() {
        val calendar = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 1, 10, 0, 0)
        }
        val plantingDate = calendar.timeInMillis
        val plant = Plant(
            name = "Cabai Rawit",
            plantingDateMillis = plantingDate
        )

        val sameDayCheck = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 1, 16, 30, 0)
        }.timeInMillis

        val hst = plant.calculateCurrentHst(sameDayCheck)
        assertEquals(0, hst)
        assertEquals("HST 0 (Hari Tanam)", plant.getHstDisplay(sameDayCheck))
    }

    @Test
    fun testPlantHstCalculation_14DaysLater_IsHst14() {
        val calendar = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 1, 8, 0, 0)
        }
        val plantingDate = calendar.timeInMillis
        val plant = Plant(
            name = "Tomat Servo",
            plantingDateMillis = plantingDate,
            targetHarvestHst = 75
        )

        val checkDate = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 15, 9, 0, 0)
        }.timeInMillis

        val hst = plant.calculateCurrentHst(checkDate)
        assertEquals(14, hst)
        assertEquals("HST 14", plant.getHstDisplay(checkDate))
    }

    @Test
    fun testPlantHstCalculation_FuturePlanting_IsNegative() {
        val futureCalendar = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 10, 0, 0, 0)
        }
        val plant = Plant(
            name = "Padi Inpari",
            plantingDateMillis = futureCalendar.timeInMillis
        )

        val checkDate = Calendar.getInstance().apply {
            set(2026, Calendar.OCTOBER, 7, 12, 0, 0)
        }.timeInMillis

        val hst = plant.calculateCurrentHst(checkDate)
        assertEquals(-3, hst)
        assertEquals("H-3 (Pra-Tanam)", plant.getHstDisplay(checkDate))
    }

    @Test
    fun testScheduleStatusCalculation() {
        val schedule = CareSchedule(
            id = 1,
            plantId = 1,
            targetHst = 14,
            careType = CareType.FERTILIZING,
            title = "Pemupukan Susulan Cor NPK"
        )

        // Target HST 14, current HST 14 -> TODAY
        assertEquals(ScheduleStatus.TODAY, schedule.getStatus(currentHst = 14))

        // Target HST 14, current HST 10 -> UPCOMING
        assertEquals(ScheduleStatus.UPCOMING, schedule.getStatus(currentHst = 10))

        // Target HST 14, current HST 18 (not completed) -> OVERDUE
        assertEquals(ScheduleStatus.OVERDUE, schedule.getStatus(currentHst = 18))

        // If completed -> COMPLETED regardless of HST
        val completedSchedule = schedule.copy(isCompleted = true)
        assertEquals(ScheduleStatus.COMPLETED, completedSchedule.getStatus(currentHst = 18))
    }

    @Test
    fun testTemplatesValidation() {
        val templates = PlantTemplate.getAllTemplates()
        assertTrue("Templates should not be empty", templates.isNotEmpty())

        val chili = templates.firstOrNull { it.id == "cabai" }
        assertTrue("Cabai template should exist", chili != null)
        assertTrue("Cabai tasks should not be empty", chili!!.tasks.isNotEmpty())

        val rice = templates.firstOrNull { it.id == "padi" }
        assertTrue("Padi template should exist", rice != null)
        assertEquals(110, rice!!.estimatedHarvestHst)
    }
}
