package com.hamdan.forzenbook.compose.core.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val COLOR_LION_YELLOW = Color(0xffffe226)

val darkTheme = darkColorScheme(
    background = COLOR_LION_YELLOW,
    primary = Color.Black,
    onPrimary = Color.White,
    tertiary = Color(0xFFeeeeee),
    surface = Color.DarkGray,
    onSurface = Color.White,
    onError = Color.Red,
    onBackground = Color.Black,
)

val lightTheme = lightColorScheme(
    background = COLOR_LION_YELLOW,
    primary = Color.Black,
    onPrimary = Color.White,
    tertiary = Color(0xFFeeeeee),
    surface = Color.DarkGray,
    onSurface = Color.White,
    onError = Color.Red,
    onBackground = Color.Black,
)
