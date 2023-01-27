package com.hamdan.forzenbook.view

import androidx.compose.material.darkColors
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import com.hamdan.forzenbook.theme.Dimens
import com.hamdan.forzenbook.theme.ReplacementColors
import com.hamdan.forzenbook.theme.ReplacementTypography
import com.hamdan.forzenbook.theme.normalDimens

val LocalNavController = compositionLocalOf<NavHostController?> { null }
/**
 * this Default Typography is just used as a default that will be overriden in the theme
 */
val LocalReplacementTypography = staticCompositionLocalOf {
    ReplacementTypography(
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
    ReplacementColors(
        colors = darkColors()
    )
}

/**
 * normalDimens is just used as a default that will be overriden in the theme
 */
val LocalDimens = staticCompositionLocalOf {
    normalDimens()
}
