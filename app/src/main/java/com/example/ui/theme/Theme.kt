package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = PrimaryLightIndigo,
    onPrimaryContainer = OnPrimaryContainerIndigo,
    secondary = SecondaryAmber,
    onSecondary = Color.White,
    tertiary = TertiaryRose,
    background = BackgroundSlate,
    onBackground = TextPrimaryDark,
    surface = SurfacePureWhite,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondaryMuted,
    outline = BorderSoft
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF80CBC4),
    onPrimary = Color(0xFF00372E),
    primaryContainer = Color(0xFF005043),
    onPrimaryContainer = Color(0xFFB2DFDB),
    secondary = Color(0xFFFFB74D),
    onSecondary = Color(0xFF4A2800),
    tertiary = Color(0xFFF48FB1),
    background = Color(0xFF121B1A),
    onBackground = Color(0xFFE0E3E2),
    surface = Color(0xFF1B2524),
    onSurface = Color(0xFFE0E3E2),
    surfaceVariant = Color(0xFF263332),
    onSurfaceVariant = Color(0xFFA0ACAA),
    outline = Color(0xFF3F4B49)
)

@Composable
fun DailyRoutineTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep tailored brand theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
