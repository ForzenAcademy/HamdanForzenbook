package com.hamdan.forzenbook.core

// used in several modules so it is available here
data class PostData(
    val posterFirstName: String,
    val posterLastName: String,
    val posterLocation: String,
    val posterIcon: String,
    val posterId: Int,
    val postId: Int,
    val body: String,
    val type: String,
    val date: String,
) {
    companion object {
        const val IMAGE_TYPE: String = "image"
        const val TEXT_TYPE: String = "text"
    }
}
