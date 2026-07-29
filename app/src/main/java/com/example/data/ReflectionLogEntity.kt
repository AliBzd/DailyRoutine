package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reflection_logs")
data class ReflectionLogEntity(
    @PrimaryKey
    val dateString: String, // YYYY-MM-DD
    val moodRating: String = "GREAT",
    val energyLevel: Int = 4, // 1 to 5
    val dailyWin: String = "",
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
