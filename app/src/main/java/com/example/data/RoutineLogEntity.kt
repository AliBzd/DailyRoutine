package com.example.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_logs",
    indices = [Index(value = ["routineItemId", "dateString"], unique = true)]
)
data class RoutineLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val routineItemId: Int,
    val dateString: String, // YYYY-MM-DD
    val isCompleted: Boolean = true,
    val completedAtTimestamp: Long = System.currentTimeMillis()
)
