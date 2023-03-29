package com.hamdan.forzenbook.compose.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimens(
    stroke: Dp,
    smallPad_1: Dp,
    smallPad_2: Dp,
    mediumPad_1: Dp,
    mediumPad_2: Dp,
    largePad_1: Dp,
    largePad_2: Dp,
    iconSizeSmall: Dp,
    iconSizeMedium: Dp,
    iconSizeLarge: Dp,
    buttonHeight: Dp,
    buttonPadHorizontal_1: Dp,
    buttonPadHorizontal_2: Dp,
    buttonPadVertical_1: Dp,
    buttonPadVertical_2: Dp,
) {
    var borderStroke by mutableStateOf(stroke, structuralEqualityPolicy())
    var smallPad_1 by mutableStateOf(smallPad_1, structuralEqualityPolicy())
    var smallPad_2 by mutableStateOf(smallPad_2, structuralEqualityPolicy())
    var mediumPad_1 by mutableStateOf(mediumPad_1, structuralEqualityPolicy())
    var mediumPad_2 by mutableStateOf(mediumPad_2, structuralEqualityPolicy())
    var largePad_1 by mutableStateOf(largePad_1, structuralEqualityPolicy())
    var largePad_2 by mutableStateOf(largePad_2, structuralEqualityPolicy())
    var iconSizeSmall by mutableStateOf(iconSizeSmall, structuralEqualityPolicy())
    var iconSizeMedium by mutableStateOf(iconSizeMedium, structuralEqualityPolicy())
    var iconSizeLarge by mutableStateOf(iconSizeLarge, structuralEqualityPolicy())
    var buttonHeight by mutableStateOf(buttonHeight, structuralEqualityPolicy())
    var buttonPadHorizontal_1 by mutableStateOf(buttonPadHorizontal_1, structuralEqualityPolicy())
    var buttonPadHorizontal_2 by mutableStateOf(buttonPadHorizontal_2, structuralEqualityPolicy())
    var buttonPadVertical_1 by mutableStateOf(buttonPadVertical_1, structuralEqualityPolicy())
    var buttonPadVertical_2 by mutableStateOf(buttonPadVertical_2, structuralEqualityPolicy())
}

fun normalDimens(
    stroke: Dp = StrokeValues.stroke2,
    smallPad_1: Dp = PaddingValues.smallPad_2,
    smallPad_2: Dp = PaddingValues.smallPad_3,
    mediumPad_1: Dp = PaddingValues.medPad_2,
    mediumPad_2: Dp = PaddingValues.medPad_3,
    largePad_1: Dp = PaddingValues.largePad_1,
    largePad_2: Dp = PaddingValues.largePad_4,
    iconSizeSmall: Dp = IconSizeValues.small_1,
    iconSizeMedium: Dp = IconSizeValues.small_2,
    iconSizeLarge: Dp = IconSizeValues.giga_1,
    buttonHeight: Dp = ButtonHeights.medium_1,
    buttonPadHorizontal_1: Dp = PaddingValues.medPad_2,
    buttonPadHorizontal_2: Dp = PaddingValues.largePad_4,
    buttonPadVertical_1: Dp = PaddingValues.smallPad_2,
    buttonPadVertical_2: Dp = PaddingValues.medPad_2,
): Dimens = Dimens(
    stroke,
    smallPad_1,
    smallPad_2,
    mediumPad_1,
    mediumPad_2,
    largePad_1,
    largePad_2,
    iconSizeSmall,
    iconSizeMedium,
    iconSizeLarge,
    buttonHeight,
    buttonPadHorizontal_1,
    buttonPadHorizontal_2,
    buttonPadVertical_1,
    buttonPadVertical_2
)

fun smallDimens(
    stroke: Dp = StrokeValues.stroke1,
    smallPad_1: Dp = PaddingValues.smallPad_1,
    smallPad_2: Dp = PaddingValues.smallPad_2,
    mediumPad_1: Dp = PaddingValues.medPad_1,
    mediumPad_2: Dp = PaddingValues.medPad_2,
    largePad_1: Dp = PaddingValues.medPad_3,
    largePad_2: Dp = PaddingValues.largePad_3,
    iconSizeSmall: Dp = IconSizeValues.small_1,
    iconSizeMedium: Dp = IconSizeValues.small_2,
    iconSizeLarge: Dp = IconSizeValues.big_1,
    buttonHeight: Dp = ButtonHeights.small_2,
    buttonPadHorizontal_1: Dp = PaddingValues.medPad_1,
    buttonPadHorizontal_2: Dp = PaddingValues.largePad_3,
    buttonPadVertical_1: Dp = PaddingValues.smallPad_1,
    buttonPadVertical_2: Dp = PaddingValues.medPad_1,
): Dimens = Dimens(
    stroke,
    smallPad_1,
    smallPad_2,
    mediumPad_1,
    mediumPad_2,
    largePad_1,
    largePad_2,
    iconSizeSmall,
    iconSizeMedium,
    iconSizeLarge,
    buttonHeight,
    buttonPadHorizontal_1,
    buttonPadHorizontal_2,
    buttonPadVertical_1,
    buttonPadVertical_2
)

object ButtonHeights {
    val small_1 = 40.dp
    val small_2 = 60.dp
    val medium_1 = 80.dp
    val medium_2 = 100.dp
    val large_1 = 120.dp
    val large_2 = 140.dp
}

object PaddingValues {
    val smallPad_1 = 4.dp
    val smallPad_2 = 8.dp
    val smallPad_3 = 12.dp
    val medPad_1 = 16.dp
    val medPad_2 = 20.dp
    val medPad_3 = 24.dp
    val largePad_1 = 28.dp
    val largePad_2 = 32.dp
    val largePad_3 = 36.dp
    val largePad_4 = 40.dp
}

object IconSizeValues {
    val small_1 = 20.dp
    val small_2 = 40.dp
    val med_1 = 60.dp
    val med_2 = 80.dp
    val large_1 = 120.dp
    val large_2 = 140.dp
    val big_1 = 160.dp
    val big_2 = 180.dp
    val giga_1 = 200.dp
    val giga_2 = 220.dp
}

object StrokeValues {
    val stroke1 = 2.dp
    val stroke2 = 4.dp
}
