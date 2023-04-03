package com.hamdan.forzenbook.post.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCase
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCase
import kotlinx.coroutines.launch

abstract class BasePostViewModel(
    val sendTextPostUseCase: SendTextPostUseCase,
    val sendImagePostUseCase: SendImagePostUseCase,
) : ViewModel() {
    sealed interface PostContent {
        data class Text(val text: String = "", val maxLength: Int = POST_LENGTH_LIMIT) : PostContent
        data class Image(val uri: String = "") : PostContent
    }

    sealed interface PostState {
        data class Content(val content: PostContent) : PostState
        object Error : PostState
        object Loading : PostState
    }

    protected abstract var postState: PostState

    fun toggleClicked() {
        toggle()
    }

    private fun toggle() {
        postState = PostState.Content(
            if ((postState as PostState.Content).content is PostContent.Text) PostContent.Image()
            else PostContent.Text()
        )
    }

    fun updateText(text: String) {
        if (text.length < POST_LENGTH_LIMIT) postState = PostState.Content(PostContent.Text(text))
    }

    fun sendPostClicked() {
        val content = (postState as PostState.Content).content
        when (true) {
            (content is PostContent.Text) -> {
                sendText()
            }
            (content is PostContent.Image) -> {
                sendImage()
            }
            else -> {
                throw Exception("illegal unknown post type")
            }
        }
    }

    private fun sendText() {
        val text = postState.getText()
        postState = PostState.Loading
        viewModelScope.launch {
            try {
                sendTextPostUseCase(PLACEHOLDER_TOKEN, text)
                postState = PostState.Content(PostContent.Text())
            } catch (e: Exception) {
                postState = PostState.Error
            }
        }
    }

    private fun sendImage() {
        val uri = postState.getUri()
        viewModelScope.launch {
            try {
                sendImagePostUseCase(PLACEHOLDER_TOKEN) // Todo when further implementation for image is added, send the bitmap
                postState = PostState.Content(PostContent.Image())
            } catch (e: Exception) {
                postState = PostState.Error
            }
        }
    }

    fun dialogDismissClicked() {
        postState = PostState.Content(PostContent.Text())
    }

    companion object {
        private const val POST_LENGTH_LIMIT = 256
        private const val PLACEHOLDER_TOKEN = "qHIc17fdLgcL750SOpxajnA9aUzDRsTDPAZqJuBcwfkcXw7rfC0mCvXzh1yed0RD"
    }
}

fun BasePostViewModel.PostState.getText(): String {
    return ((this as BasePostViewModel.PostState.Content).content as BasePostViewModel.PostContent.Text).text
}

fun BasePostViewModel.PostState.getUri(): String {
    return ((this as BasePostViewModel.PostState.Content).content as BasePostViewModel.PostContent.Image).uri
}
