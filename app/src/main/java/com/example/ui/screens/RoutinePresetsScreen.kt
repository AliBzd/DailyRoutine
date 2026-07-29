package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EveningIndigo
import com.example.ui.theme.MorningAmber
import com.example.ui.theme.PrimaryTeal
import com.example.ui.theme.SecondaryAmber

data class PresetPackage(
    val key: String,
    val title: String,
    val category: String,
    val description: String,
    val icon: ImageVector,
    val themeColor: Color,
    val items: List<String>
)

@Composable
fun RoutinePresetsScreen(
    onAddPreset: (key: String, title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val presets = listOf(
        PresetPackage(
            key = "MINDFUL_MORNING",
            title = "Mindful Morning Ritual",
            category = "MORNING",
            description = "Start your day with calm energy, hydration, and mindful movement.",
            icon = Icons.Default.WbSunny,
            themeColor = MorningAmber,
            items = listOf(
                "Hydrate & Drink 500ml Water (07:00 AM)",
                "10-Min Morning Mindfulness Meditation (07:15 AM)",
                "Light Stretches & Wake Up Movement (07:30 AM)"
            )
        ),
        PresetPackage(
            key = "DEEP_WORK",
            title = "Deep Work & Focus",
            category = "PRODUCTIVITY",
            description = "Maximize productivity with clear daily priorities and focused blocks.",
            icon = Icons.Default.WorkOutline,
            themeColor = PrimaryTeal,
            items = listOf(
                "Plan Top 3 Daily Focus Tasks (08:45 AM)",
                "90-Min Focus Deep Work Block (09:00 AM)",
                "Midday Desk & Eye Reset (02:30 PM)"
            )
        ),
        PresetPackage(
            key = "EVENING_WIND_DOWN",
            title = "Evening Wind Down",
            category = "EVENING",
            description = "Disconnect from work and prepare your mind for deep restful sleep.",
            icon = Icons.Default.NightsStay,
            themeColor = EveningIndigo,
            items = listOf(
                "Digital Sunset - Screens Off (09:00 PM)",
                "Read 15 Pages of a Book (09:30 PM)",
                "Nightly Reflection & Journaling (10:00 PM)"
            )
        ),
        PresetPackage(
            key = "HEALTH_VITALITY",
            title = "Health & Vitality",
            category = "WELLNESS",
            description = "Keep your body active and nourished throughout the day.",
            icon = Icons.Default.FitnessCenter,
            themeColor = SecondaryAmber,
            items = listOf(
                "10,000 Daily Steps Goal (05:00 PM)",
                "Healthy Dinner & Mindful Eating (06:30 PM)"
            )
        )
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        item {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Presets",
                        tint = PrimaryTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "QUICK ROUTINE PRESETS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Tap to automatically add pre-configured habits and routines to your schedule.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        items(presets) { preset ->
            PresetCard(
                preset = preset,
                onAdd = { onAddPreset(preset.key, preset.title) },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun PresetCard(
    preset: PresetPackage,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("preset_card_${preset.key}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = preset.themeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = preset.icon,
                            contentDescription = preset.title,
                            tint = preset.themeColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        color = preset.themeColor.copy(alpha = 0.12f),
                        shape = CircleShape
                    ) {
                        Text(
                            text = preset.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = preset.themeColor
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = preset.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = preset.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                preset.items.forEach { itemText ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = preset.themeColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = itemText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onAdd,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_preset_btn_${preset.key}"),
                colors = ButtonDefaults.buttonColors(containerColor = preset.themeColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add ${preset.title} Package", fontWeight = FontWeight.Bold)
            }
        }
    }
}
