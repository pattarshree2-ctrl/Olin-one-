package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = HubCyanAccent,
    onPrimary = HubDarkBg,
    primaryContainer = HubSurfaceElevated,
    onPrimaryContainer = HubCyanAccentLight,
    secondary = HubIndigoAccent,
    onSecondary = HubDarkBg,
    secondaryContainer = HubSurfaceDark,
    onSecondaryContainer = HubIndigoAccent,
    tertiary = HubVioletAccent,
    onTertiary = HubDarkBg,
    background = HubDarkBg,
    onBackground = HubTextPrimary,
    surface = HubSurfaceDark,
    onSurface = HubTextPrimary,
    surfaceVariant = HubSurfaceElevated,
    onSurfaceVariant = HubTextSecondary,
    outline = HubCardBorder,
    outlineVariant = HubBorderDark,
    error = HubRedError
)

private val LightColorScheme = lightColorScheme(
    primary = HubCyanAccentDark,
    onPrimary = HubTextPrimary,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = HubCyanAccentDark,
    secondary = HubIndigoAccent,
    onSecondary = HubTextPrimary,
    background = HubLightBg,
    onBackground = Color(0xFF0F172A),
    surface = HubSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = HubBorderLight,
    error = HubRedError
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to rich dark mode matching user prototype
    dynamicColor: Boolean = false, // Keep intentional cyan/deep space theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
