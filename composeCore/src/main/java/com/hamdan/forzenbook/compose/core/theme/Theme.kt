package com.hamdan.forzenbook.compose.core.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private const val SMALL_SCREEN_DP_WIDTH = 320
private const val DISABLED_ALPHA = .38f

// This style of theming is more flexible as it allows the creation of more themes easily
@Composable
fun ForzenBookTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) {
        darkTheme
    } else {
        lightTheme
    }

    val typography = typographyDefault

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = Shapes(),
        content = content
    )
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

private val SmallSheetSizes = SheetSizes(
    small = 200.dp,
    normal = 250.dp,
    large = 300.dp,
)

private val NormalSheetSizes = SheetSizes(
    small = 400.dp,
    normal = 500.dp,
    large = 600.dp,
)

private val NormalImages = ImageSizes(24.dp, 40.dp, 60.dp, 200.dp)
private val SmallImages = ImageSizes(24.dp, 40.dp, 60.dp, 160.dp)

val MaterialTheme.staticDimens: Dimens
    @Composable
    get() = Dimens(
        NormalGrid,
        borderDimensionsStatic,
        NormalImages,
        NormalSheetSizes,
    )
val MaterialTheme.dimens: Dimens
    @Composable
    get() = if (LocalConfiguration.current.screenWidthDp <= SMALL_SCREEN_DP_WIDTH) {
        // small
        Dimens(
            SmallGrid,
            borderDimensionsStatic,
            SmallImages,
            SmallSheetSizes,
        )
    } else {
        // average
        Dimens(
            NormalGrid,
            borderDimensionsStatic,
            NormalImages,
            NormalSheetSizes,
        )
    }

val MaterialTheme.additionalColors: AdditionalColors
    @Composable
    get() = AdditionalColors(
        inputFieldContainer = dayNightColor(light = Color(0xFFeeeeee), dark = Color(0xFFeeeeee)),
        onInputFieldContainer = dayNightColor(light = Color.Black, dark = Color.Black),
        spacerColor = colorScheme.onSurface.copy(alpha = .15f),
        sheetColor = dayNightColor(light = Color.White, dark = Color(0xFF111111)),
        sheetHandle = COLOR_LION_YELLOW,
        onSheetBorder = dayNightColor(light = Color(0xFFaaaaaa), dark = Color(0xFFdddddd)),
        onBackgroundBorder = dayNightColor(light = Color(0xFF444444), dark = Color(0xFFaaaaaa)),
        onDisabledButton = Color.White,
    )

val MaterialTheme.disabledAlpha: Float
    get() = DISABLED_ALPHA
