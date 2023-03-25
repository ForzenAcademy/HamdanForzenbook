package com.hamdan.forzenbook.post.core.viewmodel

import androidx.lifecycle.ViewModel

abstract class BasePostViewModel() : ViewModel() {
    data class PostState(
        val postText: String = "",
        val postType: PostType = PostType.TEXT,
        val isLoading: Boolean = false,
    )

    protected abstract var postState: PostState

    fun toggleClicked() {
        toggle()
    }

    private fun toggle() {
        postState = postState.copy(
            postType = if (postState.postType == PostType.TEXT) PostType.IMAGE else PostType.TEXT
        )
    }

    fun updateText(text: String) {
        if (text.length < POST_LENGTH_LIMIT) postState = postState.copy(postText = text)
    }

    companion object {
        private const val POST_LENGTH_LIMIT = 256
    }
}

enum class PostType {
    IMAGE, TEXT
}
