package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_items")
data class RoutineItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val timeOfDay: String = TimeOfDay.MORNING.name,
    val targetTime: String = "08:00 AM",
    val iconKey: String = "check",
    val colorHex: String = "#00897B",
    val isHabit: Boolean = true,
    val streakCount: Int = 0,
    val bestStreak: Int = 0,
    val isArchived: Boolean = false
)
