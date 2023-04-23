package com.hamdan.forzenbook.compose.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalReplacementColors

private const val SMALL_SCREEN_DP_WIDTH = 320

data class ReplacementColors(
    val colors: ColorScheme
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
    val replacementColors: ReplacementColors = if (isSystemInDarkTheme()) {
        ReplacementColors(darkTheme)
    } else {
        ReplacementColors(lightTheme)
    }
    CompositionLocalProvider(
        LocalReplacementColors provides replacementColors,
    ) {
        MaterialTheme(
            colorScheme = replacementColors.colors,
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

private val borderDimensionsStatic = BorderDimensions(
    1.dp, 2.dp, 3.dp, 4.dp,
)

private val NormalImages = ImageSizes(40.dp, 60.dp, 200.dp)
private val SmallImages = ImageSizes(40.dp, 60.dp, 160.dp)

object ForzenbookTheme {
    val colors: ReplacementColors
        @Composable
        get() = LocalReplacementColors.current

    val ForzenbookTheme.dimens: Dimens
        @Composable
        get() = if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
            // small
            Dimens(
                SmallGrid,
                borderDimensionsStatic,
                NormalGrid,
                SmallImages
            )
        } else {
            // average
            Dimens(
                NormalGrid,
                borderDimensionsStatic,
                NormalGrid,
                NormalImages
            )
        }

    val ForzenbookTheme.typography: androidx.compose.material3.Typography
        @Composable
        get() = if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
            // small
            typographySmall
        } else {
            // average
            typographyDefault
        }
}
