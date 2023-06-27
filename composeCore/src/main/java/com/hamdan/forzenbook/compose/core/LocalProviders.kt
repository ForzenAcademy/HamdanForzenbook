package com.hamdan.forzenbook.compose.core

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

// used to provide navigator at all levels
val LocalNavController = compositionLocalOf<NavHostController?> { null }
