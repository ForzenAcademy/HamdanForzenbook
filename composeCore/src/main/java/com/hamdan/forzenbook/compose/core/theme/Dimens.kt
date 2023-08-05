package com.hamdan.forzenbook.compose.core.theme

import androidx.compose.ui.unit.Dp

data class GridDimensions(
    val x1: Dp,
    val x2: Dp,
    val x3: Dp,
    val x4: Dp,
    val x5: Dp,
    val x6: Dp,
    val x7: Dp,
    val x8: Dp,
    val x9: Dp,
    val x10: Dp,
    val x11: Dp,
    val x12: Dp,
    val x13: Dp,
    val x14: Dp,
    val x15: Dp,
    val x16: Dp,
    val x17: Dp,
    val x18: Dp,
    val x19: Dp,
    val x20: Dp,
)

data class ImageSizes(
    val tiny: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
)

data class BorderDimensions(
    val x1: Dp,
    val x2: Dp,
    val x3: Dp,
    val x4: Dp,
)

data class Dimens(
    val grid: GridDimensions,
    val borderGrid: BorderDimensions,
    val imageSizes: ImageSizes,
    val sheetSizes: SheetSizes,
)

data class SheetSizes(
    val small: Dp,
    val normal: Dp,
    val large: Dp,
)
