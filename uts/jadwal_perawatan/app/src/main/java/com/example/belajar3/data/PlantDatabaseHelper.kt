package com.example.belajar3.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.belajar3.model.CareSchedule
import com.example.belajar3.model.CareType
import com.example.belajar3.model.Plant

class PlantDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "plant_care_hst.db"
        private const val DATABASE_VERSION = 1

        // Table Plants
        const val TABLE_PLANTS = "plants"
        const val COL_PLANT_ID = "id"
        const val COL_PLANT_NAME = "name"
        const val COL_PLANT_VARIETY = "variety"
        const val COL_PLANTING_DATE = "planting_date_millis"
        const val COL_PLOT_LOCATION = "plot_location"
        const val COL_TARGET_HARVEST_HST = "target_harvest_hst"
        const val COL_REMINDER_HOUR = "reminder_hour"
        const val COL_REMINDER_MINUTE = "reminder_minute"
        const val COL_IS_REMINDER_ENABLED = "is_reminder_enabled"
        const val COL_PLANT_NOTES = "notes"
        const val COL_CREATED_AT = "created_at"

        // Table Care Schedules
        const val TABLE_CARE_SCHEDULES = "care_schedules"
        const val COL_SCHEDULE_ID = "id"
        const val COL_SCHEDULE_PLANT_ID = "plant_id"
        const val COL_TARGET_HST = "target_hst"
        const val COL_CARE_TYPE = "care_type"
        const val COL_SCHEDULE_TITLE = "title"
        const val COL_SCHEDULE_DESC = "description"
        const val COL_IS_COMPLETED = "is_completed"
        const val COL_COMPLETED_DATE = "completed_date_millis"
        const val COL_COMPLETION_NOTES = "completion_notes"

        @Volatile
        private var instance: PlantDatabaseHelper? = null

        fun getInstance(context: Context): PlantDatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: PlantDatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createPlantsTable = """
            CREATE TABLE $TABLE_PLANTS (
                $COL_PLANT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PLANT_NAME TEXT NOT NULL,
                $COL_PLANT_VARIETY TEXT,
                $COL_PLANTING_DATE INTEGER NOT NULL,
                $COL_PLOT_LOCATION TEXT,
                $COL_TARGET_HARVEST_HST INTEGER DEFAULT 90,
                $COL_REMINDER_HOUR INTEGER DEFAULT 7,
                $COL_REMINDER_MINUTE INTEGER DEFAULT 0,
                $COL_IS_REMINDER_ENABLED INTEGER DEFAULT 1,
                $COL_PLANT_NOTES TEXT,
                $COL_CREATED_AT INTEGER
            )
        """.trimIndent()

        val createCareSchedulesTable = """
            CREATE TABLE $TABLE_CARE_SCHEDULES (
                $COL_SCHEDULE_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_SCHEDULE_PLANT_ID INTEGER NOT NULL,
                $COL_TARGET_HST INTEGER NOT NULL,
                $COL_CARE_TYPE TEXT NOT NULL,
                $COL_SCHEDULE_TITLE TEXT NOT NULL,
                $COL_SCHEDULE_DESC TEXT,
                $COL_IS_COMPLETED INTEGER DEFAULT 0,
                $COL_COMPLETED_DATE INTEGER,
                $COL_COMPLETION_NOTES TEXT,
                FOREIGN KEY ($COL_SCHEDULE_PLANT_ID) REFERENCES $TABLE_PLANTS($COL_PLANT_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createPlantsTable)
        db.execSQL(createCareSchedulesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CARE_SCHEDULES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PLANTS")
        onCreate(db)
    }

    // --- PLANT OPERATIONS ---

    fun insertPlant(plant: Plant): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PLANT_NAME, plant.name)
            put(COL_PLANT_VARIETY, plant.variety)
            put(COL_PLANTING_DATE, plant.plantingDateMillis)
            put(COL_PLOT_LOCATION, plant.plotLocation)
            put(COL_TARGET_HARVEST_HST, plant.targetHarvestHst)
            put(COL_REMINDER_HOUR, plant.reminderHour)
            put(COL_REMINDER_MINUTE, plant.reminderMinute)
            put(COL_IS_REMINDER_ENABLED, if (plant.isReminderEnabled) 1 else 0)
            put(COL_PLANT_NOTES, plant.notes)
            put(COL_CREATED_AT, plant.createdAt)
        }
        return db.insert(TABLE_PLANTS, null, values)
    }

    fun updatePlant(plant: Plant): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_PLANT_NAME, plant.name)
            put(COL_PLANT_VARIETY, plant.variety)
            put(COL_PLANTING_DATE, plant.plantingDateMillis)
            put(COL_PLOT_LOCATION, plant.plotLocation)
            put(COL_TARGET_HARVEST_HST, plant.targetHarvestHst)
            put(COL_REMINDER_HOUR, plant.reminderHour)
            put(COL_REMINDER_MINUTE, plant.reminderMinute)
            put(COL_IS_REMINDER_ENABLED, if (plant.isReminderEnabled) 1 else 0)
            put(COL_PLANT_NOTES, plant.notes)
        }
        return db.update(TABLE_PLANTS, values, "$COL_PLANT_ID = ?", arrayOf(plant.id.toString()))
    }

    fun deletePlant(plantId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_PLANTS, "$COL_PLANT_ID = ?", arrayOf(plantId.toString()))
    }

    fun getAllPlants(): List<Plant> {
        val list = mutableListOf<Plant>()
        val db = readableDatabase
        val cursor = db.query(TABLE_PLANTS, null, null, null, null, null, "$COL_PLANT_ID DESC")
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToPlant(it))
            }
        }
        return list
    }

    fun getPlantById(id: Long): Plant? {
        val db = readableDatabase
        val cursor = db.query(TABLE_PLANTS, null, "$COL_PLANT_ID = ?", arrayOf(id.toString()), null, null, null)
        cursor.use {
            if (it.moveToNext()) {
                return cursorToPlant(it)
            }
        }
        return null
    }

    private fun cursorToPlant(c: Cursor): Plant {
        return Plant(
            id = c.getLong(c.getColumnIndexOrThrow(COL_PLANT_ID)),
            name = c.getString(c.getColumnIndexOrThrow(COL_PLANT_NAME)),
            variety = c.getString(c.getColumnIndexOrThrow(COL_PLANT_VARIETY)) ?: "",
            plantingDateMillis = c.getLong(c.getColumnIndexOrThrow(COL_PLANTING_DATE)),
            plotLocation = c.getString(c.getColumnIndexOrThrow(COL_PLOT_LOCATION)) ?: "",
            targetHarvestHst = c.getInt(c.getColumnIndexOrThrow(COL_TARGET_HARVEST_HST)),
            reminderHour = c.getInt(c.getColumnIndexOrThrow(COL_REMINDER_HOUR)),
            reminderMinute = c.getInt(c.getColumnIndexOrThrow(COL_REMINDER_MINUTE)),
            isReminderEnabled = c.getInt(c.getColumnIndexOrThrow(COL_IS_REMINDER_ENABLED)) == 1,
            notes = c.getString(c.getColumnIndexOrThrow(COL_PLANT_NOTES)) ?: "",
            createdAt = c.getLong(c.getColumnIndexOrThrow(COL_CREATED_AT))
        )
    }

    // --- CARE SCHEDULE OPERATIONS ---

    fun insertCareSchedule(schedule: CareSchedule): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_SCHEDULE_PLANT_ID, schedule.plantId)
            put(COL_TARGET_HST, schedule.targetHst)
            put(COL_CARE_TYPE, schedule.careType.name)
            put(COL_SCHEDULE_TITLE, schedule.title)
            put(COL_SCHEDULE_DESC, schedule.description)
            put(COL_IS_COMPLETED, if (schedule.isCompleted) 1 else 0)
            put(COL_COMPLETED_DATE, schedule.completedDateMillis)
            put(COL_COMPLETION_NOTES, schedule.completionNotes)
        }
        return db.insert(TABLE_CARE_SCHEDULES, null, values)
    }

    fun insertCareSchedules(schedules: List<CareSchedule>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (schedule in schedules) {
                val values = ContentValues().apply {
                    put(COL_SCHEDULE_PLANT_ID, schedule.plantId)
                    put(COL_TARGET_HST, schedule.targetHst)
                    put(COL_CARE_TYPE, schedule.careType.name)
                    put(COL_SCHEDULE_TITLE, schedule.title)
                    put(COL_SCHEDULE_DESC, schedule.description)
                    put(COL_IS_COMPLETED, if (schedule.isCompleted) 1 else 0)
                    put(COL_COMPLETED_DATE, schedule.completedDateMillis)
                    put(COL_COMPLETION_NOTES, schedule.completionNotes)
                }
                db.insert(TABLE_CARE_SCHEDULES, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun updateCareSchedule(schedule: CareSchedule): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TARGET_HST, schedule.targetHst)
            put(COL_CARE_TYPE, schedule.careType.name)
            put(COL_SCHEDULE_TITLE, schedule.title)
            put(COL_SCHEDULE_DESC, schedule.description)
            put(COL_IS_COMPLETED, if (schedule.isCompleted) 1 else 0)
            put(COL_COMPLETED_DATE, schedule.completedDateMillis)
            put(COL_COMPLETION_NOTES, schedule.completionNotes)
        }
        return db.update(TABLE_CARE_SCHEDULES, values, "$COL_SCHEDULE_ID = ?", arrayOf(schedule.id.toString()))
    }

    fun deleteCareSchedule(id: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_CARE_SCHEDULES, "$COL_SCHEDULE_ID = ?", arrayOf(id.toString()))
    }

    fun getSchedulesForPlant(plantId: Long): List<CareSchedule> {
        val list = mutableListOf<CareSchedule>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CARE_SCHEDULES,
            null,
            "$COL_SCHEDULE_PLANT_ID = ?",
            arrayOf(plantId.toString()),
            null,
            null,
            "$COL_TARGET_HST ASC, $COL_SCHEDULE_ID ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToSchedule(it))
            }
        }
        return list
    }

    fun getAllSchedules(): List<CareSchedule> {
        val list = mutableListOf<CareSchedule>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CARE_SCHEDULES,
            null,
            null,
            null,
            null,
            null,
            "$COL_TARGET_HST ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToSchedule(it))
            }
        }
        return list
    }

    fun setScheduleCompletion(id: Long, completed: Boolean, completionTimeMillis: Long?, notes: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_IS_COMPLETED, if (completed) 1 else 0)
            put(COL_COMPLETED_DATE, if (completed) (completionTimeMillis ?: System.currentTimeMillis()) else null)
            put(COL_COMPLETION_NOTES, notes)
        }
        return db.update(TABLE_CARE_SCHEDULES, values, "$COL_SCHEDULE_ID = ?", arrayOf(id.toString()))
    }

    private fun cursorToSchedule(c: Cursor): CareSchedule {
        val careTypeStr = c.getString(c.getColumnIndexOrThrow(COL_CARE_TYPE))
        val careType = try {
            CareType.valueOf(careTypeStr)
        } catch (_: Exception) {
            CareType.OTHER
        }

        val completedDateCol = c.getColumnIndexOrThrow(COL_COMPLETED_DATE)
        val completedDate = if (c.isNull(completedDateCol)) null else c.getLong(completedDateCol)

        return CareSchedule(
            id = c.getLong(c.getColumnIndexOrThrow(COL_SCHEDULE_ID)),
            plantId = c.getLong(c.getColumnIndexOrThrow(COL_SCHEDULE_PLANT_ID)),
            targetHst = c.getInt(c.getColumnIndexOrThrow(COL_TARGET_HST)),
            careType = careType,
            title = c.getString(c.getColumnIndexOrThrow(COL_SCHEDULE_TITLE)),
            description = c.getString(c.getColumnIndexOrThrow(COL_SCHEDULE_DESC)) ?: "",
            isCompleted = c.getInt(c.getColumnIndexOrThrow(COL_IS_COMPLETED)) == 1,
            completedDateMillis = completedDate,
            completionNotes = c.getString(c.getColumnIndexOrThrow(COL_COMPLETION_NOTES)) ?: ""
        )
    }
}
