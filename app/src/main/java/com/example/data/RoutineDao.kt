package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    // Routine Items
    @Query("SELECT * FROM routine_items WHERE isArchived = 0 ORDER BY id ASC")
    fun getAllActiveRoutines(): Flow<List<RoutineItemEntity>>

    @Query("SELECT * FROM routine_items WHERE id = :id LIMIT 1")
    suspend fun getRoutineById(id: Int): RoutineItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(item: RoutineItemEntity): Long

    @Update
    suspend fun updateRoutine(item: RoutineItemEntity)

    @Delete
    suspend fun deleteRoutine(item: RoutineItemEntity)

    @Query("DELETE FROM routine_items WHERE id = :id")
    suspend fun deleteRoutineById(id: Int)

    // Routine Logs
    @Query("SELECT * FROM routine_logs WHERE dateString = :dateString")
    fun getLogsForDate(dateString: String): Flow<List<RoutineLogEntity>>

    @Query("SELECT * FROM routine_logs WHERE routineItemId = :routineItemId AND dateString = :dateString LIMIT 1")
    suspend fun getLogForRoutineAndDate(routineItemId: Int, dateString: String): RoutineLogEntity?

    @Query("SELECT * FROM routine_logs WHERE routineItemId = :routineItemId AND isCompleted = 1 ORDER BY dateString DESC")
    fun getCompletedLogsForRoutine(routineItemId: Int): Flow<List<RoutineLogEntity>>

    @Query("SELECT * FROM routine_logs ORDER BY dateString DESC")
    fun getAllLogs(): Flow<List<RoutineLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLog(log: RoutineLogEntity)

    @Query("DELETE FROM routine_logs WHERE routineItemId = :routineItemId AND dateString = :dateString")
    suspend fun deleteLogForRoutineAndDate(routineItemId: Int, dateString: String)

    // Reflection Logs
    @Query("SELECT * FROM reflection_logs WHERE dateString = :dateString LIMIT 1")
    fun getReflectionForDateFlow(dateString: String): Flow<ReflectionLogEntity?>

    @Query("SELECT * FROM reflection_logs WHERE dateString = :dateString LIMIT 1")
    suspend fun getReflectionForDate(dateString: String): ReflectionLogEntity?

    @Query("SELECT * FROM reflection_logs ORDER BY dateString DESC")
    fun getAllReflections(): Flow<List<ReflectionLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateReflection(reflection: ReflectionLogEntity)
}
