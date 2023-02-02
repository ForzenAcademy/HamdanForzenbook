package com.hamdan.forzenbook.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hamdan.forzenbook.R

val defaultFontFamily = FontFamily(
    Font(R.font.mont_light, FontWeight.Light),
    Font(R.font.mont_light_ital, FontWeight.Light, FontStyle.Italic),
    Font(R.font.mont_medium, FontWeight.Medium),
    Font(R.font.mont_medium_ital, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.mont_bold, FontWeight.Bold),
    Font(R.font.mont_bold_ital, FontWeight.Bold, FontStyle.Italic),
)

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

    /**
     * 40.sp
     */
    val large_4 = 44.sp
}
