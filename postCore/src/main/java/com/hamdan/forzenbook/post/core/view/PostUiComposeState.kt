package com.hamdan.forzenbook.post.core.view

import android.graphics.Bitmap
import com.hamdan.forzenbook.post.core.viewmodel.PostType

data class PostUiComposeState(
    val postText: String = "",
    val postImage: Bitmap? = null,
    val postType: PostType = PostType.TEXT,
    val hasError: Boolean = false,
    val isLoading: Boolean = false,
)
