package com.hamdan.forzenbook.post.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.post.core.domain.SendImagePostUseCase
import com.hamdan.forzenbook.post.core.domain.SendTextPostUseCase
import kotlinx.coroutines.launch
import java.io.File

abstract class BasePostViewModel(
    val sendTextPostUseCase: SendTextPostUseCase,
    val sendImagePostUseCase: SendImagePostUseCase,
) : ViewModel() {
    sealed interface PostContent {
        data class Text(val text: String = "", val maxLength: Int = POST_LENGTH_LIMIT) : PostContent
        data class Image(val filePath: String? = null) : PostContent
    }

    sealed interface PostState {
        data class Content(val content: PostContent) : PostState
        object Error : PostState
        object Loading : PostState
        object InvalidLogin : PostState
    }

    protected abstract var postState: PostState

    fun toggleClicked() = toggle()

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
        when ((postState as PostState.Content).content) {
            is PostContent.Image -> {
                sendImage()
            }

            is PostContent.Text -> {
                sendText()
            }
        }
    }

    fun updateImage(filePath: String?) {
        // in the case the user immediately picks another image, delete the old temp file and create a new one
        postState = PostState.Content(PostContent.Image(filePath))
    }

    private fun sendText() {
        postState.asTextOrNull()?.let {
            postState = PostState.Loading
            viewModelScope.launch {
                try {
                    sendTextPostUseCase(it.text)
                    postState = PostState.Content(PostContent.Text())
                } catch (e: InvalidTokenException) {
                    postState = PostState.InvalidLogin
                } catch (e: Exception) {
                    postState = PostState.Error
                }
            }
        }
    }

    private fun sendImage() {
        postState.asImageOrNull()?.let {
            it.filePath?.let { path ->
                viewModelScope.launch {
                    try {
                        sendImagePostUseCase(path)
                        File(path).delete()
                        postState = PostState.Content(PostContent.Image())
                    } catch (e: InvalidTokenException) {
                        File(path).delete()
                        postState = PostState.InvalidLogin
                    } catch (e: Exception) {
                        File(path).delete()
                        postState = PostState.Error
                    }
                }
            }
        }
    }

    fun kickBackToLogin() {
        // intending to just reset the viewmodel here and have the view navigate to login
        postState = PostState.Content(PostContent.Text())
    }

    fun dialogDismissClicked() {
        postState = PostState.Content(PostContent.Text())
    }

    companion object {
        private const val POST_LENGTH_LIMIT = 256
    }
}

fun BasePostViewModel.PostState.asContentOrNull(): BasePostViewModel.PostContent? {
    return (this as? BasePostViewModel.PostState.Content)?.content
}

fun BasePostViewModel.PostState.asImageOrNull(): BasePostViewModel.PostContent.Image? {
    return this.asContentOrNull() as? BasePostViewModel.PostContent.Image
}

fun BasePostViewModel.PostState.asTextOrNull(): BasePostViewModel.PostContent.Text? {
    return this.asContentOrNull() as? BasePostViewModel.PostContent.Text
}
