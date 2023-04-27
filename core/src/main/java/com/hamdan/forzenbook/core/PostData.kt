package com.hamdan.forzenbook.core

data class PostData(
    val posterName: String,
    val posterLocation: String,
    val posterIcon: String?,
    val posterId: Int,
    val body: String,
    val type: String,
    val date: String,
) {
    companion object {
        const val IMAGE_TYPE: String = "image"
        const val TEXT_TYPE: String = "text"
    }
}
