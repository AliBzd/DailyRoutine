package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.ui.graphics.vector.ImageVector

object RoutineIconMapper {

    val iconMap = mapOf(
        "water" to Icons.Default.WaterDrop,
        "meditate" to Icons.Default.SelfImprovement,
        "exercise" to Icons.Default.FitnessCenter,
        "walk" to Icons.Default.DirectionsWalk,
        "run" to Icons.Default.DirectionsRun,
        "read" to Icons.Default.MenuBook,
        "journal" to Icons.Default.EditNote,
        "work" to Icons.Default.WorkOutline,
        "sleep" to Icons.Default.Bedtime,
        "sun" to Icons.Default.WbSunny,
        "heart" to Icons.Default.Favorite,
        "coffee" to Icons.Default.LocalCafe,
        "spa" to Icons.Default.Spa,
        "timer" to Icons.Default.Timer,
        "check" to Icons.Default.CheckCircle
    )

    fun getIcon(key: String): ImageVector {
        return iconMap[key] ?: Icons.Default.CheckCircle
    }
}
