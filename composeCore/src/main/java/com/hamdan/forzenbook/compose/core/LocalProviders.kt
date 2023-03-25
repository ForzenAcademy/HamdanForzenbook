package com.hamdan.forzenbook.compose.core

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavController = compositionLocalOf<NavHostController?> { null }

/**
 * darkColors is just used as a default that will be overriden in the theme
 */
val LocalReplacementColors = staticCompositionLocalOf {
    com.hamdan.forzenbook.compose.core.theme.ReplacementColors(
        colors = darkColorScheme()
    )
}
