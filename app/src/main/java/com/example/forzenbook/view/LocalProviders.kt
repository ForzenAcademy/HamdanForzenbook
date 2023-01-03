package com.example.forzenbook.view

import androidx.compose.material.darkColors
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import com.example.forzenbook.theme.ReplacementColors
import com.example.forzenbook.theme.ReplacementTypography

val LocalNavController = compositionLocalOf<NavHostController?> { null }
val LocalReplacementTypography = staticCompositionLocalOf {
    ReplacementTypography(
        defaultFontFamily = FontFamily.Default,
        h1 = TextStyle.Default,
        button = TextStyle.Default,
        h3 = TextStyle.Default,
        h4 = TextStyle.Default,
    )
}

val LocalReplacementColors = staticCompositionLocalOf {
    ReplacementColors(
        colors = darkColors()
    )
}