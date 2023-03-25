package com.hamdan.forzenbook.post.core.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCase
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCase
import com.hamdan.forzenbook.post.core.view.PostUiComposeState
import kotlinx.coroutines.launch

abstract class BasePostViewModel(
    val sendTextPostUseCase: SendTextPostUseCase,
    val sendImagePostUseCase: SendImagePostUseCase,
) : ViewModel() {
    data class PostState(
        val postText: String = "",
        val postImage: Bitmap? = null,
        val postType: PostType = PostType.TEXT,
        val hasError: Boolean = false,
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

    fun sendPostClicked() {
        postState = postState.copy(isLoading = true)
        if (postState.postType == PostType.TEXT) sendText() else sendImage()
    }

    private fun sendText() {
        viewModelScope.launch {
            try {
                sendTextPostUseCase(PLACEHOLDER_TOKEN, postState.postText)
                postState = postState.copy(isLoading = false)
            } catch (e: Exception) {
                postState = postState.copy(hasError = true, isLoading = false)
            }
        }
    }

    private fun sendImage() {
        viewModelScope.launch {
            try {
                postState.postImage?.apply {
                    sendImagePostUseCase(PLACEHOLDER_TOKEN) // Todo when further implementation for image is added, send the bitmap
                    postState = postState.copy(isLoading = false)
                }
            } catch (e: Exception) {
                postState = postState.copy(hasError = true, isLoading = false)
            }
        }
    }

    fun dialogDismissClicked() {
        postState = postState.copy(hasError = false)
    }

    companion object {
        private const val POST_LENGTH_LIMIT = 256
        private const val PLACEHOLDER_TOKEN = "123456asd"
    }
}

enum class PostType {
    IMAGE, TEXT
}

fun BasePostViewModel.PostState.toUiState(): PostUiComposeState {
    return PostUiComposeState(
        this.postText,
        this.postImage,
        this.postType,
        this.hasError,
        this.isLoading
    )
}

fun PostType.toBoolean(): Boolean = this == PostType.TEXT
