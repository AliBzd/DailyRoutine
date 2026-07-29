package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RoutineItemEntity
import com.example.data.TimeOfDay
import com.example.ui.components.HeaderProgressCard
import com.example.ui.components.RoutineIconMapper
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.StreakFlameOrange

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleTimelineScreen(
    displayDateFormatted: String,
    isToday: Boolean,
    routines: List<RoutineItemEntity>,
    completedRoutineIds: Set<Int>,
    selectedFilter: TimeOfDay?,
    searchQuery: String,
    maxStreak: Int,
    onPreviousDate: () -> Unit,
    onNextDate: () -> Unit,
    onTodayClick: () -> Unit,
    onFilterChange: (TimeOfDay?) -> Unit,
    onToggleCompletion: (RoutineItemEntity) -> Unit,
    onEditRoutine: (RoutineItemEntity) -> Unit,
    onDeleteRoutine: (Int) -> Unit,
    onAddRoutineClick: () -> Unit,
    onPresetsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = routines.count { completedRoutineIds.contains(it.id) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header Progress Component
        item {
            HeaderProgressCard(
                displayDateFormatted = displayDateFormatted,
                isToday = isToday,
                completedCount = completedCount,
                totalCount = routines.size,
                maxStreak = maxStreak,
                onPreviousDate = onPreviousDate,
                onNextDate = onNextDate,
                onTodayClick = onTodayClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Filter Pills Row
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = "TIME OF DAY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilter == null,
                            onClick = { onFilterChange(null) },
                            label = { Text("All (${routines.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryTeal,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("filter_all")
                        )
                    }

                    items(TimeOfDay.values()) { tod ->
                        val countForTod = routines.count { it.timeOfDay == tod.name }
                        FilterChip(
                            selected = selectedFilter == tod,
                            onClick = { onFilterChange(tod) },
                            label = {
                                Text("${tod.label} ($countForTod)")
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Timeline Routine Cards
        if (routines.isEmpty()) {
            item {
                EmptyRoutineView(
                    searchQuery = searchQuery,
                    onAddRoutineClick = onAddRoutineClick,
                    onPresetsClick = onPresetsClick
                )
            }
        } else {
            items(routines, key = { it.id }) { routine ->
                val isCompleted = completedRoutineIds.contains(routine.id)
                RoutineItemCard(
                    routine = routine,
                    isCompleted = isCompleted,
                    onToggleCompletion = { onToggleCompletion(routine) },
                    onEditRoutine = { onEditRoutine(routine) },
                    onDeleteRoutine = { onDeleteRoutine(routine.id) },
                    modifier = Modifier
                        .animateItemPlacement(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun RoutineItemCard(
    routine: RoutineItemEntity,
    isCompleted: Boolean,
    onToggleCompletion: () -> Unit,
    onEditRoutine: () -> Unit,
    onDeleteRoutine: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    val cardBgColor by animateColorAsState(
        targetValue = if (isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else MaterialTheme.colorScheme.surface,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "card_bg"
    )

    val checkboxScale by animateFloatAsState(
        targetValue = if (isCompleted) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "checkbox_scale"
    )

    val itemColor = try {
        Color(android.graphics.Color.parseColor(routine.colorHex))
    } catch (e: Exception) {
        PrimaryTeal
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("routine_item_${routine.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Circle with spring bounce
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .graphicsLayer {
                        scaleX = checkboxScale
                        scaleY = checkboxScale
                    }
                    .clip(CircleShape)
                    .background(if (isCompleted) PrimaryTeal else Color.Transparent)
                    .border(
                        width = 2.dp,
                        color = if (isCompleted) PrimaryTeal else itemColor,
                        shape = CircleShape
                    )
                    .clickable { onToggleCompletion() }
                    .testTag("checkbox_routine_${routine.id}"),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isCompleted,
                    enter = fadeIn() + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                    exit = fadeOut() + scaleOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Icon Badge
            Surface(
                color = itemColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = RoutineIconMapper.getIcon(routine.iconKey),
                        contentDescription = routine.title,
                        tint = itemColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = routine.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                if (routine.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = routine.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Time Badge & Streak Tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Target Time",
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${routine.timeOfDay.lowercase().capitalize()} • ${routine.targetTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (routine.isHabit && routine.streakCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Streak",
                                modifier = Modifier.size(12.dp),
                                tint = StreakFlameOrange
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${routine.streakCount}d",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = StreakFlameOrange
                            )
                        }
                    }
                }
            }

            // More Options Menu
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.testTag("menu_button_${routine.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit Routine") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = {
                            showMenu = false
                            onEditRoutine()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                        leadingIcon = { Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            onDeleteRoutine()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyRoutineView(
    searchQuery: String,
    onAddRoutineClick: () -> Unit,
    onPresetsClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PlaylistAdd,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = PrimaryTeal.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (searchQuery.isNotEmpty()) "No routines matching \"$searchQuery\""
                else "No routines scheduled here",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Build healthy habits by adding custom routines or loading quick presets.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onAddRoutineClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                    modifier = Modifier.testTag("empty_add_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Custom")
                }

                OutlinedButton(
                    onClick = onPresetsClick,
                    modifier = Modifier.testTag("empty_presets_button")
                ) {
                    Text("Browse Presets")
                }
            }
        }
    }
}
