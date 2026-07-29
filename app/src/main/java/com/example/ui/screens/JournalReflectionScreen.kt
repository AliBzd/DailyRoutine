package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ReflectionLogEntity
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryAmber

data class MoodItem(val code: String, val label: String, val emoji: String)

val MoodOptions = listOf(
    MoodItem("GREAT", "Great", "😄"),
    MoodItem("CALM", "Calm", "🧘"),
    MoodItem("FOCUSED", "Focused", "🎯"),
    MoodItem("ENERGETIC", "Energetic", "⚡"),
    MoodItem("TIRED", "Tired", "😴"),
    MoodItem("STRESSED", "Stressed", "😮‍💨")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JournalReflectionScreen(
    displayDateFormatted: String,
    reflection: ReflectionLogEntity?,
    onSaveReflection: (mood: String, energy: Int, win: String, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMood by remember { mutableStateOf("GREAT") }
    var energyLevel by remember { mutableStateOf(4) }
    var dailyWinText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }

    // Sync input when reflection changes
    LaunchedEffect(reflection) {
        if (reflection != null) {
            selectedMood = reflection.moodRating
            energyLevel = reflection.energyLevel
            dailyWinText = reflection.dailyWin
            notesText = reflection.notes
        } else {
            selectedMood = "GREAT"
            energyLevel = 4
            dailyWinText = ""
            notesText = ""
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reflection_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Book,
                            contentDescription = "Journal",
                            tint = PrimaryTeal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "DAILY REFLECTION & JOURNAL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = displayDateFormatted,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Mood Selector
                    Text(
                        text = "How are you feeling today?",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MoodOptions.forEach { mood ->
                            FilterChip(
                                selected = selectedMood == mood.code,
                                onClick = { selectedMood = mood.code },
                                label = {
                                    Text("${mood.emoji} ${mood.label}")
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PrimaryTeal,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("mood_chip_${mood.code}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 2. Energy Level Scale
                    Text(
                        text = "Energy Level ($energyLevel/5)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (level in 1..5) {
                            val isSelected = level <= energyLevel
                            Icon(
                                imageVector = if (isSelected) Icons.Default.Bolt else Icons.Default.Bolt,
                                contentDescription = "Energy $level",
                                tint = if (isSelected) SecondaryAmber else MaterialTheme.colorScheme.outline,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { energyLevel = level }
                                    .testTag("energy_level_$level")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 3. Today's Win
                    OutlinedTextField(
                        value = dailyWinText,
                        onValueChange = { dailyWinText = it },
                        label = { Text("Today's Biggest Win 🎉") },
                        placeholder = { Text("e.g., Completed my focus block and drank 2L water!") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("daily_win_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. Notes & Reflection
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("Daily Reflection & Thoughts ✍️") },
                        placeholder = { Text("What went well today? What can be improved tomorrow?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .testTag("daily_notes_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { onSaveReflection(selectedMood, energyLevel, dailyWinText, notesText) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_reflection_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Journal Entry", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
