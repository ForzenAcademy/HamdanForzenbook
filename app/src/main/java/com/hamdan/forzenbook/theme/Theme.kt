package com.hamdan.forzenbook.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.hamdan.forzenbook.view.LocalDimens
import com.hamdan.forzenbook.view.LocalReplacementColors
import com.hamdan.forzenbook.view.LocalReplacementTypography

private const val SMALL_SCREEN_DP_WIDTH = 320

data class ReplacementColors(
    val colors: Colors
)

data class ReplacementTypography(
    val defaultFontFamily: FontFamily,
    val h1: TextStyle,
    val h2: TextStyle,
    val button: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle
)

@SuppressLint("ConflictingOnColor")
@Composable
fun ForzenBookTheme(content: @Composable () -> Unit) {
    val replacementTypography = if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
        ReplacementTypography(
            defaultFontFamily = defaultFontFamily,
            h1 = TextStyle(fontSize = TextSizeValues.large_2),
            h2 = TextStyle(fontSize = TextSizeValues.med_2),
            button = TextStyle(fontSize = TextSizeValues.med_1),
            h3 = TextStyle(fontSize = TextSizeValues.small_3),
            h4 = TextStyle(fontSize = TextSizeValues.small_2),
        )
    } else {
        ReplacementTypography(
            defaultFontFamily = defaultFontFamily,
            h1 = TextStyle(fontSize = TextSizeValues.large_4),
            h2 = TextStyle(fontSize = TextSizeValues.large_1),
            button = TextStyle(fontSize = TextSizeValues.med_3),
            h3 = TextStyle(fontSize = TextSizeValues.med_1),
            h4 = TextStyle(fontSize = TextSizeValues.small_3),
        )
    }
    val replacementColors: ReplacementColors = if (isSystemInDarkTheme()) {
        ReplacementColors(darkTheme)
    } else {
        ReplacementColors(lightTheme)
    }
    val adaptiveDimens: Dimens = if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
        // small screens
        smallDimens()
    } else {
        // average
        normalDimens()
    }
    CompositionLocalProvider(
        LocalReplacementTypography provides replacementTypography,
        LocalReplacementColors provides replacementColors,
        LocalDimens provides adaptiveDimens
    ) {
        MaterialTheme(
            colors = replacementColors.colors,
            typography = Typography(defaultFontFamily = replacementTypography.defaultFontFamily),
            content = content
        )
    }
}

object ForzenbookTheme {
    val typography: ReplacementTypography
        @Composable
        get() = LocalReplacementTypography.current
    val colors: ReplacementColors
        @Composable
        get() = LocalReplacementColors.current
    val dimens: Dimens
        @Composable
        get() = LocalDimens.current
}
