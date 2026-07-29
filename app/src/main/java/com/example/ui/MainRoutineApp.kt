package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddEditRoutineDialog
import com.example.ui.screens.HabitsStreaksScreen
import com.example.ui.screens.JournalReflectionScreen
import com.example.ui.screens.RoutinePresetsScreen
import com.example.ui.screens.ScheduleTimelineScreen
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.StreakFlameOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRoutineApp(
    viewModel: RoutineViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var isSearchActive by remember { mutableStateOf(false) }

    val maxStreak = state.routines.maxOfOrNull { it.streakCount } ?: 0

    // Show toast messages as SnackBar
    LaunchedEffect(state.messageToast) {
        state.messageToast?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearMessageToast()
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search routines & habits...") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("top_search_input")
                        )
                    } else {
                        Text(
                            text = "Daily Routine",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            isSearchActive = !isSearchActive
                            if (!isSearchActive) viewModel.setSearchQuery("")
                        },
                        modifier = Modifier.testTag("top_search_button")
                    ) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("bottom_navigation"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = state.activeTab == 0,
                    onClick = { viewModel.setActiveTab(0) },
                    icon = { Icon(Icons.Default.Today, contentDescription = "Schedule") },
                    label = { Text("Schedule") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryTeal,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_schedule")
                )

                NavigationBarItem(
                    selected = state.activeTab == 1,
                    onClick = { viewModel.setActiveTab(1) },
                    icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = "Streaks") },
                    label = { Text("Streaks") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = StreakFlameOrange,
                        selectedTextColor = StreakFlameOrange,
                        indicatorColor = StreakFlameOrange.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_streaks")
                )

                NavigationBarItem(
                    selected = state.activeTab == 2,
                    onClick = { viewModel.setActiveTab(2) },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Presets") },
                    label = { Text("Presets") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryTeal,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_presets")
                )

                NavigationBarItem(
                    selected = state.activeTab == 3,
                    onClick = { viewModel.setActiveTab(3) },
                    icon = { Icon(Icons.Default.Book, contentDescription = "Journal") },
                    label = { Text("Journal") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryTeal,
                        selectedTextColor = PrimaryTeal,
                        indicatorColor = PrimaryTeal.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("tab_journal")
                )
            }
        },
        floatingActionButton = {
            if (state.activeTab == 0 || state.activeTab == 1) {
                FloatingActionButton(
                    onClick = { viewModel.openAddDialog() },
                    containerColor = PrimaryTeal,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.testTag("fab_add_routine")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Routine")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (state.activeTab) {
                0 -> ScheduleTimelineScreen(
                    displayDateFormatted = state.displayDateFormatted,
                    isToday = state.isToday,
                    routines = state.routines,
                    completedRoutineIds = state.completedRoutineIds,
                    selectedFilter = state.selectedTimeOfDayFilter,
                    searchQuery = state.searchQuery,
                    maxStreak = maxStreak,
                    onPreviousDate = { viewModel.navigateDateBy(-1) },
                    onNextDate = { viewModel.navigateDateBy(1) },
                    onTodayClick = { viewModel.setToday() },
                    onFilterChange = { viewModel.setTimeOfDayFilter(it) },
                    onToggleCompletion = { viewModel.toggleRoutineCompletion(it) },
                    onEditRoutine = { viewModel.openEditDialog(it) },
                    onDeleteRoutine = { viewModel.deleteRoutine(it) },
                    onAddRoutineClick = { viewModel.openAddDialog() },
                    onPresetsClick = { viewModel.setActiveTab(2) }
                )

                1 -> HabitsStreaksScreen(
                    routines = state.routines,
                    completedRoutineIds = state.completedRoutineIds,
                    onToggleCompletion = { viewModel.toggleRoutineCompletion(it) }
                )

                2 -> RoutinePresetsScreen(
                    onAddPreset = { key, title -> viewModel.addPresetPackage(key, title) }
                )

                3 -> JournalReflectionScreen(
                    displayDateFormatted = state.displayDateFormatted,
                    reflection = state.reflection,
                    onSaveReflection = { mood, energy, win, notes ->
                        viewModel.saveReflection(mood, energy, win, notes)
                    }
                )
            }
        }
    }

    // Add / Edit Dialog
    if (state.isAddEditOpen) {
        AddEditRoutineDialog(
            editingRoutine = state.editingRoutine,
            onDismiss = { viewModel.closeAddEditDialog() },
            onSave = { title, desc, tod, targetTime, iconKey, colorHex ->
                viewModel.saveRoutine(title, desc, tod, targetTime, iconKey, colorHex)
            }
        )
    }
}
