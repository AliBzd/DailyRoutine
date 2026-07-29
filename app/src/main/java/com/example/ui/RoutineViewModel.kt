package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ReflectionLogEntity
import com.example.data.RoutineItemEntity
import com.example.data.RoutineLogEntity
import com.example.data.RoutineRepository
import com.example.data.TimeOfDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class RoutineUiState(
    val selectedDateString: String = RoutineRepository.getTodayDateString(),
    val displayDateFormatted: String = RoutineRepository.formatDateDisplay(RoutineRepository.getTodayDateString()),
    val isToday: Boolean = true,
    val routines: List<RoutineItemEntity> = emptyList(),
    val completedRoutineIds: Set<Int> = emptySet(),
    val reflection: ReflectionLogEntity? = null,
    val selectedTimeOfDayFilter: TimeOfDay? = null,
    val searchQuery: String = "",
    val activeTab: Int = 0, // 0 = Schedule, 1 = Habits & Streaks, 2 = Presets, 3 = Journal
    val editingRoutine: RoutineItemEntity? = null,
    val isAddEditOpen: Boolean = false,
    val messageToast: String? = null
)

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoutineRepository

    private val _selectedDate = MutableStateFlow(RoutineRepository.getTodayDateString())
    private val _timeOfDayFilter = MutableStateFlow<TimeOfDay?>(null)
    private val _searchQuery = MutableStateFlow("")
    private val _activeTab = MutableStateFlow(0)
    private val _editingRoutine = MutableStateFlow<RoutineItemEntity?>(null)
    private val _isAddEditOpen = MutableStateFlow(false)
    private val _messageToast = MutableStateFlow<String?>(null)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = RoutineRepository(database.routineDao())

        // Seed initial routines if empty
        viewModelScope.launch {
            repository.seedDefaultDataIfEmpty()
        }
    }

    private val logsForSelectedDate = _selectedDate.flatMapLatest { dateStr ->
        repository.getLogsForDate(dateStr)
    }

    private val reflectionForSelectedDate = _selectedDate.flatMapLatest { dateStr ->
        repository.getReflectionForDate(dateStr)
    }

    private data class DataState(
        val dateStr: String,
        val routines: List<RoutineItemEntity>,
        val logs: List<RoutineLogEntity>,
        val reflection: ReflectionLogEntity?
    )

    private data class FilterState(
        val filter: TimeOfDay?,
        val query: String,
        val tab: Int
    )

    private data class DialogState(
        val editing: RoutineItemEntity?,
        val isOpen: Boolean,
        val msg: String?
    )

    private data class UiControlState(
        val filter: TimeOfDay?,
        val query: String,
        val tab: Int,
        val editing: RoutineItemEntity?,
        val isOpen: Boolean,
        val msg: String?
    )

    private val dataFlow = combine(
        _selectedDate,
        repository.allRoutines,
        logsForSelectedDate,
        reflectionForSelectedDate
    ) { dateStr, routines, logs, reflection ->
        DataState(dateStr, routines, logs, reflection)
    }

    private val filterFlow = combine(
        _timeOfDayFilter,
        _searchQuery,
        _activeTab
    ) { filter, query, tab ->
        FilterState(filter, query, tab)
    }

    private val dialogFlow = combine(
        _editingRoutine,
        _isAddEditOpen,
        _messageToast
    ) { editing, isOpen, msg ->
        DialogState(editing, isOpen, msg)
    }

    private val uiControlFlow = combine(filterFlow, dialogFlow) { f, d ->
        UiControlState(f.filter, f.query, f.tab, d.editing, d.isOpen, d.msg)
    }

    val uiState: StateFlow<RoutineUiState> = combine(dataFlow, uiControlFlow) { data, control ->
        val completedIds = data.logs.filter { it.isCompleted }.map { it.routineItemId }.toSet()

        val filteredRoutines = data.routines.filter { routine ->
            val matchesFilter = control.filter == null || routine.timeOfDay == control.filter.name
            val matchesQuery = control.query.isEmpty() ||
                    routine.title.contains(control.query, ignoreCase = true) ||
                    routine.description.contains(control.query, ignoreCase = true)
            matchesFilter && matchesQuery
        }

        RoutineUiState(
            selectedDateString = data.dateStr,
            displayDateFormatted = RoutineRepository.formatDateDisplay(data.dateStr),
            isToday = data.dateStr == RoutineRepository.getTodayDateString(),
            routines = filteredRoutines,
            completedRoutineIds = completedIds,
            reflection = data.reflection,
            selectedTimeOfDayFilter = control.filter,
            searchQuery = control.query,
            activeTab = control.tab,
            editingRoutine = control.editing,
            isAddEditOpen = control.isOpen,
            messageToast = control.msg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RoutineUiState()
    )

    fun setSelectedDate(dateString: String) {
        _selectedDate.value = dateString
    }

    fun navigateDateBy(days: Int) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(_selectedDate.value) ?: Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_YEAR, days)
            _selectedDate.value = sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setToday() {
        _selectedDate.value = RoutineRepository.getTodayDateString()
    }

    fun setTimeOfDayFilter(filter: TimeOfDay?) {
        _timeOfDayFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setActiveTab(tab: Int) {
        _activeTab.value = tab
    }

    fun toggleRoutineCompletion(routine: RoutineItemEntity) {
        viewModelScope.launch {
            repository.toggleRoutineCompletion(routine, _selectedDate.value)
        }
    }

    fun openAddDialog() {
        _editingRoutine.value = null
        _isAddEditOpen.value = true
    }

    fun openEditDialog(routine: RoutineItemEntity) {
        _editingRoutine.value = routine
        _isAddEditOpen.value = true
    }

    fun closeAddEditDialog() {
        _isAddEditOpen.value = false
        _editingRoutine.value = null
    }

    fun saveRoutine(
        title: String,
        description: String,
        timeOfDay: TimeOfDay,
        targetTime: String,
        iconKey: String,
        colorHex: String
    ) {
        if (title.isBlank()) return

        viewModelScope.launch {
            val current = _editingRoutine.value
            if (current == null) {
                repository.insertRoutine(
                    RoutineItemEntity(
                        title = title.trim(),
                        description = description.trim(),
                        timeOfDay = timeOfDay.name,
                        targetTime = targetTime.trim(),
                        iconKey = iconKey,
                        colorHex = colorHex,
                        isHabit = true
                    )
                )
                _messageToast.value = "Routine item added!"
            } else {
                repository.updateRoutine(
                    current.copy(
                        title = title.trim(),
                        description = description.trim(),
                        timeOfDay = timeOfDay.name,
                        targetTime = targetTime.trim(),
                        iconKey = iconKey,
                        colorHex = colorHex
                    )
                )
                _messageToast.value = "Routine updated!"
            }
            closeAddEditDialog()
        }
    }

    fun deleteRoutine(routineId: Int) {
        viewModelScope.launch {
            repository.deleteRoutine(routineId)
            _messageToast.value = "Routine deleted"
        }
    }

    fun addPresetPackage(presetKey: String, presetTitle: String) {
        viewModelScope.launch {
            repository.addPresetRoutinePackage(presetKey)
            _messageToast.value = "Added $presetTitle routines!"
        }
    }

    fun saveReflection(mood: String, energy: Int, win: String, notes: String) {
        viewModelScope.launch {
            repository.saveReflection(
                ReflectionLogEntity(
                    dateString = _selectedDate.value,
                    moodRating = mood,
                    energyLevel = energy,
                    dailyWin = win.trim(),
                    notes = notes.trim(),
                    timestamp = System.currentTimeMillis()
                )
            )
            _messageToast.value = "Journal entry saved!"
        }
    }

    fun clearMessageToast() {
        _messageToast.value = null
    }
}
