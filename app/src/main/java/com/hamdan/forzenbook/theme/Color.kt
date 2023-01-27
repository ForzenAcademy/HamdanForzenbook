package com.hamdan.forzenbook.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val COLOR_LION_YELLOW = Color(0xffffe226)

val darkTheme = darkColors(
    background = COLOR_LION_YELLOW,
    primary = Color.Black,
    onPrimary = Color.White,
    secondaryVariant = Color.White,
    surface = Color.DarkGray,
    onSurface = Color.White,
    onError = Color.Red,
    onBackground = Color.Black,
)

val lightTheme = lightColors(
    background = COLOR_LION_YELLOW,
    primary = Color.Black,
    onPrimary = Color.White,
    secondaryVariant = Color.White,
    surface = Color.DarkGray,
    onSurface = Color.White,
    onError = Color.Red,
    onBackground = Color.Black,
)
