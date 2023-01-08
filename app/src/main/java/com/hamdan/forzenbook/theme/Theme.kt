package com.hamdan.forzenbook.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.view.LocalReplacementColors
import com.hamdan.forzenbook.view.LocalReplacementTypography
import javax.annotation.concurrent.Immutable

@Immutable
data class ReplacementColors(
    val colors: Colors
)

@Immutable
data class ReplacementTypography(
    /**
     * Montserrat
     */
    val defaultFontFamily: FontFamily,
    /**
     * intended 40.sp
     */
    val h1: TextStyle,
    /**
     * intended 28.sp
     */
    val button: TextStyle,
    /**
     * 20.sp
     */
    val h3: TextStyle,
    /**
     * 16.sp
     */
    val h4: TextStyle
)
/*
TODO fit in dimens and play around with a nexus S with text scaling increased
    to make things more accurate refer to the discussion in the chat with Nic
    anything under 11pt violates ADA guidelines
 */

@SuppressLint("ConflictingOnColor")
@Composable
fun ForzenBookTheme(content: @Composable () -> Unit) {
    val replacementTypography = ReplacementTypography(
        defaultFontFamily = FontFamily(
            Font(R.font.mont_light, FontWeight.Light),
            Font(R.font.mont_light_ital, FontWeight.Light, FontStyle.Italic),
            Font(R.font.mont_medium, FontWeight.Medium),
            Font(R.font.mont_medium_ital, FontWeight.Medium, FontStyle.Italic),
            Font(R.font.mont_bold, FontWeight.Bold),
            Font(R.font.mont_bold_ital, FontWeight.Bold, FontStyle.Italic),
        ),
        h1 = TextStyle(fontSize = TextSizeValues.large_3),
        button = TextStyle(fontSize = TextSizeValues.med_3),
        h3 = TextStyle(fontSize = TextSizeValues.med_1),
        h4 = TextStyle(fontSize = TextSizeValues.small_3),
    )
    val replacementColors: ReplacementColors = if (isSystemInDarkTheme()) {
        ReplacementColors(
            darkColors(
                background = COLOR_LION_YELLOW,
                primary = BLACK,
                onPrimary = WHITE,
                secondaryVariant = WHITE,
                surface = DARK_GRAY,
                onSurface = GRAY,
                onError = Color.Red,
                onBackground = BLACK,
            )
        )
    } else {
        ReplacementColors(
            lightColors(
                background = COLOR_LION_YELLOW,
                primary = WHITE,
                onPrimary = Color.Black,
                secondaryVariant = WHITE,
                surface = DARK_GRAY,
                onSurface = GRAY,
                onError = Color.Red,
                onBackground = BLACK,
            )
        )
    }
    Log.v("Hamdan", "Color:${replacementColors.colors.secondaryVariant.toArgb()}")
    CompositionLocalProvider(
        LocalReplacementTypography provides replacementTypography,
        LocalReplacementColors provides replacementColors
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
}

object PaddingValues {
    /**
     * 4.dp
     */
    val smallPad_1 = 4.dp

    /**
     * 8.dp
     */
    val smallPad_2 = 8.dp

    /**
     * 12.dp
     */
    val smallPad_3 = 12.dp

    /**
     * 16.dp
     */
    val medPad_1 = 16.dp

    /**
     * 20.dp
     */
    val medPad_2 = 20.dp

    /**
     * 24.dp
     */
    val medPad_3 = 24.dp

    /**
     * 28.dp
     */
    val largePad_1 = 28.dp

    /**
     * 32.dp
     */
    val largePad_2 = 32.dp

    /**
     * 36.dp
     */
    val largePad_3 = 36.dp

    /**
     * 40.dp
     */
    val largePad_4 = 40.dp
}

object TextSizeValues {
    /**
     * 8.sp
     */
    val small_1 = 8.sp

    /**
     * 12.sp
     */
    val small_2 = 12.sp

    /**
     * 16.sp
     */
    val small_3 = 16.sp

    /**
     * 20.sp
     */
    val med_1 = 20.sp

    /**
     * 24.sp
     */
    val med_2 = 24.sp

    /**
     * 28.sp
     */
    val med_3 = 28.sp

    /**
     * 32.sp
     */
    val large_1 = 32.sp

    /**
     * 36.sp
     */
    val large_2 = 36.sp

    /**
     * 40.sp
     */
    val large_3 = 40.sp
}

object IconSizeValues {
    /**
     * 20.dp
     */
    val small_1 = 20.dp

    /**
     * 40.dp
     */
    val small_2 = 40.dp

    /**
     * 60.dp
     */
    val med_1 = 60.dp

    /**
     * 80.dp
     */
    val med_2 = 80.dp

    /**
     * 120.dp
     */
    val large_1 = 120.dp

    /**
     * 140.dp
     */
    val large_2 = 140.dp

    /**
     * 160.dp
     */
    val big_1 = 160.dp

    /**
     * 180.dp
     */
    val big_2 = 180.dp

    /**
     * 200.dp
     */
    val giga_1 = 200.dp

    /**
     * 220.dp
     */
    val giga_2 = 220.dp

}
