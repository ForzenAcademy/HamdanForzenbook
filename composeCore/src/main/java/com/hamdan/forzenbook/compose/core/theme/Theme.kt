package com.hamdan.forzenbook.compose.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalReplacementColors
import com.hamdan.forzenbook.compose.core.LocalReplacementTypography

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

@Composable
fun ForzenBookTheme(content: @Composable () -> Unit) {
    val replacementTypography =
        if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
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
    CompositionLocalProvider(
        LocalReplacementTypography provides replacementTypography,
        LocalReplacementColors provides replacementColors,
    ) {
        MaterialTheme(
            colors = replacementColors.colors,
            typography = Typography(defaultFontFamily = replacementTypography.defaultFontFamily),
            content = content
        )
    }
}

private val NormalGrid = GridDimensions(
    4.dp, 8.dp, 12.dp, 16.dp, 20.dp, 24.dp, 28.dp, 32.dp, 36.dp, 40.dp,
    44.dp, 48.dp, 52.dp, 56.dp, 60.dp, 64.dp, 68.dp, 72.dp, 76.dp, 80.dp,
)

private val SmallGrid = GridDimensions(
    2.dp, 4.dp, 6.dp, 8.dp, 10.dp, 12.dp, 14.dp, 16.dp, 18.dp, 20.dp,
    22.dp, 24.dp, 26.dp, 28.dp, 30.dp, 32.dp, 34.dp, 36.dp, 38.dp, 40.dp,
)

private val NormalImages = ImageSizes(40.dp, 60.dp, 200.dp)
private val SmallImages = ImageSizes(40.dp, 60.dp, 160.dp)

object ForzenbookTheme {
    val typography: ReplacementTypography
        @Composable
        get() = LocalReplacementTypography.current
    val colors: ReplacementColors
        @Composable
        get() = LocalReplacementColors.current
}

val ForzenbookTheme.dimens: Dimens
    @Composable
    get() = if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
        // small screens
        Dimens(
            SmallGrid,
            NormalGrid,
            SmallImages
        )
    } else {
        // average
        Dimens(
            NormalGrid,
            NormalGrid,
            NormalImages
        )
    }
