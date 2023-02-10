package com.hamdan.forzenbook.compose.core

import androidx.compose.material.darkColors
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

/**
 * this Default Typography is just used as a default that will be overriden in the theme
 */
val LocalReplacementTypography = staticCompositionLocalOf {
    com.hamdan.forzenbook.compose.core.theme.ReplacementTypography(
        defaultFontFamily = FontFamily.Default,
        h1 = TextStyle.Default,
        h2 = TextStyle.Default,
        button = TextStyle.Default,
        h3 = TextStyle.Default,
        h4 = TextStyle.Default,
    )
}

/**
 * darkColors is just used as a default that will be overriden in the theme
 */
val LocalReplacementColors = staticCompositionLocalOf {
    com.hamdan.forzenbook.compose.core.theme.ReplacementColors(
        colors = darkColors()
    )
}

/**
 * normalDimens is just used as a default that will be overriden in the theme
 */
val LocalDimens = staticCompositionLocalOf {
    com.hamdan.forzenbook.compose.core.theme.normalDimens()
}
