package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RoutineRepository(private val routineDao: RoutineDao) {

    val allRoutines: Flow<List<RoutineItemEntity>> = routineDao.getAllActiveRoutines()
    val allLogs: Flow<List<RoutineLogEntity>> = routineDao.getAllLogs()
    val allReflections: Flow<List<ReflectionLogEntity>> = routineDao.getAllReflections()

    fun getLogsForDate(dateString: String): Flow<List<RoutineLogEntity>> {
        return routineDao.getLogsForDate(dateString)
    }

    fun getReflectionForDate(dateString: String): Flow<ReflectionLogEntity?> {
        return routineDao.getReflectionForDateFlow(dateString)
    }

    suspend fun toggleRoutineCompletion(routine: RoutineItemEntity, dateString: String) {
        val existingLog = routineDao.getLogForRoutineAndDate(routine.id, dateString)
        if (existingLog != null && existingLog.isCompleted) {
            // Uncheck
            routineDao.deleteLogForRoutineAndDate(routine.id, dateString)
            // Decrement streak if today
            val newStreak = (routine.streakCount - 1).coerceAtLeast(0)
            routineDao.updateRoutine(routine.copy(streakCount = newStreak))
        } else {
            // Check
            routineDao.insertOrUpdateLog(
                RoutineLogEntity(
                    routineItemId = routine.id,
                    dateString = dateString,
                    isCompleted = true,
                    completedAtTimestamp = System.currentTimeMillis()
                )
            )
            val newStreak = routine.streakCount + 1
            val newBest = maxOf(newStreak, routine.bestStreak)
            routineDao.updateRoutine(routine.copy(streakCount = newStreak, bestStreak = newBest))
        }
    }

    suspend fun insertRoutine(routine: RoutineItemEntity): Long {
        return routineDao.insertRoutine(routine)
    }

    suspend fun updateRoutine(routine: RoutineItemEntity) {
        routineDao.updateRoutine(routine)
    }

    suspend fun deleteRoutine(routineId: Int) {
        routineDao.deleteRoutineById(routineId)
    }

    suspend fun saveReflection(reflection: ReflectionLogEntity) {
        routineDao.insertOrUpdateReflection(reflection)
    }

    suspend fun seedDefaultDataIfEmpty() {
        val currentItems = routineDao.getAllActiveRoutines().first()
        if (currentItems.isEmpty()) {
            getDefaultRoutines().forEach { routine ->
                routineDao.insertRoutine(routine)
            }
        }
    }

    suspend fun addPresetRoutinePackage(presetKey: String) {
        val presets = when (presetKey) {
            "MINDFUL_MORNING" -> listOf(
                RoutineItemEntity(title = "Hydrate & Drink Water", description = "500ml upon waking up", timeOfDay = TimeOfDay.MORNING.name, targetTime = "07:00 AM", iconKey = "water", colorHex = "#0288D1"),
                RoutineItemEntity(title = "Morning Meditation", description = "10 mins mindfulness breathing", timeOfDay = TimeOfDay.MORNING.name, targetTime = "07:15 AM", iconKey = "meditate", colorHex = "#7B1FA2"),
                RoutineItemEntity(title = "Light Yoga / Stretches", description = "Wake up muscles & joint mobility", timeOfDay = TimeOfDay.MORNING.name, targetTime = "07:30 AM", iconKey = "exercise", colorHex = "#E65100")
            )
            "DEEP_WORK" -> listOf(
                RoutineItemEntity(title = "Plan Top 3 Daily Focus Tasks", description = "Prioritize high value work", timeOfDay = TimeOfDay.MORNING.name, targetTime = "08:45 AM", iconKey = "work", colorHex = "#00897B"),
                RoutineItemEntity(title = "90-Min Focus Deep Work Block", description = "No distractions, phone on silent", timeOfDay = TimeOfDay.MORNING.name, targetTime = "09:00 AM", iconKey = "work", colorHex = "#00897B"),
                RoutineItemEntity(title = "Midday Desk & Eye Reset", description = "Look away from screen, walk 5m", timeOfDay = TimeOfDay.AFTERNOON.name, targetTime = "02:30 PM", iconKey = "sun", colorHex = "#FB8C00")
            )
            "EVENING_WIND_DOWN" -> listOf(
                RoutineItemEntity(title = "Digital Sunset (Screens Off)", description = "Turn off TV and bright devices", timeOfDay = TimeOfDay.EVENING.name, targetTime = "09:00 PM", iconKey = "sleep", colorHex = "#3949AB"),
                RoutineItemEntity(title = "Read 15 Pages", description = "Enjoy a calming book before bed", timeOfDay = TimeOfDay.EVENING.name, targetTime = "09:30 PM", iconKey = "read", colorHex = "#1E88E5"),
                RoutineItemEntity(title = "Nightly Reflection & Journal", description = "Write 1 win & mood note", timeOfDay = TimeOfDay.EVENING.name, targetTime = "10:00 PM", iconKey = "journal", colorHex = "#D81B60")
            )
            "HEALTH_VITALITY" -> listOf(
                RoutineItemEntity(title = "10,000 Daily Steps Goal", description = "Brisk walks throughout the day", timeOfDay = TimeOfDay.ANYTIME.name, targetTime = "05:00 PM", iconKey = "walk", colorHex = "#43A047"),
                RoutineItemEntity(title = "Healthy Meal & Mindful Eating", description = "Eat nutrient-dense dinner", timeOfDay = TimeOfDay.EVENING.name, targetTime = "06:30 PM", iconKey = "heart", colorHex = "#E53935")
            )
            else -> emptyList()
        }

        presets.forEach { routineDao.insertRoutine(it) }
    }

    private fun getDefaultRoutines(): List<RoutineItemEntity> {
        return listOf(
            RoutineItemEntity(title = "Hydrate & Drink 500ml Water", description = "Kickstart your metabolism", timeOfDay = TimeOfDay.MORNING.name, targetTime = "07:00 AM", iconKey = "water", colorHex = "#0288D1", streakCount = 3, bestStreak = 7),
            RoutineItemEntity(title = "10-Min Breathing Meditation", description = "Calm mind & center focus", timeOfDay = TimeOfDay.MORNING.name, targetTime = "07:15 AM", iconKey = "meditate", colorHex = "#7B1FA2", streakCount = 2, bestStreak = 5),
            RoutineItemEntity(title = "Plan Top 3 Priorities", description = "Set daily intentions clearly", timeOfDay = TimeOfDay.MORNING.name, targetTime = "08:30 AM", iconKey = "work", colorHex = "#00897B", streakCount = 4, bestStreak = 10),
            RoutineItemEntity(title = "5,000 Midday Steps Walk", description = "Fresh air & posture boost", timeOfDay = TimeOfDay.AFTERNOON.name, targetTime = "01:00 PM", iconKey = "walk", colorHex = "#43A047", streakCount = 1, bestStreak = 4),
            RoutineItemEntity(title = "Read 20 Pages of a Book", description = "Expand knowledge daily", timeOfDay = TimeOfDay.EVENING.name, targetTime = "08:30 PM", iconKey = "read", colorHex = "#1E88E5", streakCount = 5, bestStreak = 14),
            RoutineItemEntity(title = "Evening Reflection & Gratitude", description = "Journal today's wins", timeOfDay = TimeOfDay.EVENING.name, targetTime = "09:30 PM", iconKey = "journal", colorHex = "#D81B60", streakCount = 2, bestStreak = 6)
        )
    }

    companion object {
        fun getTodayDateString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }

        fun formatDateDisplay(dateString: String): String {
            return try {
                val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdfInput.parse(dateString) ?: Date()
                val sdfOutput = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())
                sdfOutput.format(date)
            } catch (e: Exception) {
                dateString
            }
        }
    }
}
