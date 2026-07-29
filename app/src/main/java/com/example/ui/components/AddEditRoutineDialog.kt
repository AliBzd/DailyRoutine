package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.RoutineItemEntity
import com.example.data.TimeOfDay
import com.example.ui.theme.PrimaryTeal

val AvailableColors = listOf(
    "#00897B", "#F57C00", "#7B1FA2", "#1E88E5", "#D81B60", "#43A047", "#0288D1", "#E65100"
)

val IconKeysList = listOf(
    "water", "meditate", "exercise", "walk", "run", "read", "journal", "work", "sleep", "sun", "heart", "coffee", "check"
)

val SuggestedTimes = listOf(
    "06:30 AM", "07:00 AM", "08:00 AM", "09:00 AM", "12:30 PM", "02:00 PM", "05:00 PM", "08:00 PM", "09:30 PM"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditRoutineDialog(
    editingRoutine: RoutineItemEntity?,
    onDismiss: () -> Unit,
    onSave: (
        title: String,
        description: String,
        timeOfDay: TimeOfDay,
        targetTime: String,
        iconKey: String,
        colorHex: String
    ) -> Unit
) {
    var title by remember { mutableStateOf(editingRoutine?.title ?: "") }
    var description by remember { mutableStateOf(editingRoutine?.description ?: "") }
    var selectedTimeOfDay by remember {
        mutableStateOf(
            if (editingRoutine != null) TimeOfDay.fromString(editingRoutine.timeOfDay)
            else TimeOfDay.MORNING
        )
    }
    var targetTime by remember { mutableStateOf(editingRoutine?.targetTime ?: "08:00 AM") }
    var selectedIconKey by remember { mutableStateOf(editingRoutine?.iconKey ?: "check") }
    var selectedColorHex by remember { mutableStateOf(editingRoutine?.colorHex ?: "#00897B") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (editingRoutine == null) "Add New Daily Routine" else "Edit Routine",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Routine Title *") },
                    placeholder = { Text("e.g. 15-Min Morning Stretches") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_title_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Short Description / Goal") },
                    placeholder = { Text("e.g. Drink 2 full glasses of water") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_desc_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Time of Day Selector
                Text(
                    text = "Time Block",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TimeOfDay.values().forEach { tod ->
                        FilterChip(
                            selected = selectedTimeOfDay == tod,
                            onClick = { selectedTimeOfDay = tod },
                            label = { Text(tod.label) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Target Time Suggestions
                Text(
                    text = "Target Schedule Time",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = targetTime,
                    onValueChange = { targetTime = it },
                    label = { Text("Time String") },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_target_time_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuggestedTimes.forEach { st ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape,
                            modifier = Modifier
                                .clickable { targetTime = st }
                                .padding(2.dp)
                        ) {
                            Text(
                                text = st,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Icon Selector
                Text(
                    text = "Select Icon",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconKeysList.forEach { key ->
                        val isSelected = selectedIconKey == key
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) PrimaryTeal.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = if (isSelected) PrimaryTeal else Color.Transparent,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedIconKey = key },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = RoutineIconMapper.getIcon(key),
                                contentDescription = key,
                                tint = if (isSelected) PrimaryTeal else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Color Selector
                Text(
                    text = "Theme Color",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AvailableColors.forEach { hex ->
                        val parsedColor = try {
                            Color(android.graphics.Color.parseColor(hex))
                        } catch (e: Exception) {
                            PrimaryTeal
                        }
                        val isSelected = selectedColorHex.equals(hex, ignoreCase = true)

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(parsedColor)
                                .clickable { selectedColorHex = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(
                                    title,
                                    description,
                                    selectedTimeOfDay,
                                    targetTime,
                                    selectedIconKey,
                                    selectedColorHex
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("dialog_save_button")
                    ) {
                        Text("Save Routine", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
