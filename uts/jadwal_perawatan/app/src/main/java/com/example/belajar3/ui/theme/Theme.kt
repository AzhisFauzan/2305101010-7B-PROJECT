package com.example.belajar3.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = LightGreen,
    onPrimaryContainer = DarkGreen,
    background = SoftBackground,
    surface = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderSubtle
)

@Composable
fun Belajar3Theme(
    // Force light theme by ignoring the system dark theme toggle completely
    darkTheme: Boolean = false, 
    content: @Composable () -> Unit
) {
    // Only use LightColorScheme to ensure the app is always BRIGHT, LIGHT, GREEN-AND-WHITE
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
