package com.hamdan.forzenbook.compose.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val COLOR_LION_YELLOW = Color(0xffffe226)
val LIGHT_GRAY = Color.LightGray.copy(alpha = .8f)

val darkTheme = darkColorScheme(
    background = Color.Black,
    onBackground = Color.White,
    primary = COLOR_LION_YELLOW,
    onPrimary = Color.Black,
    primaryContainer = COLOR_LION_YELLOW,
    onPrimaryContainer = Color.Black,
    tertiary = Color(0xFF111111),
    onTertiary = Color.White,
    surface = Color(0xFF222222), // standard dark theme middle ground for cards
    onSurface = Color.White,
    onSurfaceVariant = LIGHT_GRAY,
)

val lightTheme = lightColorScheme(
    background = Color.White,
    onBackground = Color.Black,
    primary = COLOR_LION_YELLOW,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF222222),
    onPrimaryContainer = Color.White,
    tertiary = Color(0xFFeeeeee),
    onTertiary = Color.Black,
    surface = Color.White, // color of cards middle ground
    onSurface = Color.Black,
    onSurfaceVariant = Color.DarkGray,
)

data class AdditionalColors(
    val inputFieldContainer: Color,
    val onInputFieldContainer: Color,
    val spacerColor: Color,
    val sheetColor: Color,
    val sheetHandle: Color,
    val onSheetBorder: Color,
    val onBackgroundBorder: Color,
    val onDisabledButton: Color,
)

@Composable
fun dayNightColor(light: Color, dark: Color): Color = if (isSystemInDarkTheme()) dark else light
