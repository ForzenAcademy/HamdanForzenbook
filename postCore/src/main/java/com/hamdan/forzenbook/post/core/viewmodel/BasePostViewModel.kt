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

    fun toggleClicked() {
        val currentState = postState
        postState = PostState.Content(
            if ((currentState as PostState.Content).content is PostContent.Text) PostContent.Image()
            else PostContent.Text()
        )
    }

    fun updateText(text: String) {
        val currentState = postState
        if (text.length <= POST_LENGTH_LIMIT && currentState is PostState.Content && currentState.content is PostContent.Text) {
            postState = PostState.Content(PostContent.Text(text))
        }
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
        val state = postState
        if (state is PostState.Content && state.content is PostContent.Text && state.content.text.isNotEmpty() && state.content.text.length <= POST_LENGTH_LIMIT) {
            postState = PostState.Loading
            viewModelScope.launch {
                try {
                    sendTextPostUseCase(state.content.text)
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
        val state = postState
        if (state is PostState.Content && state.content is PostContent.Image) {
            state.content.filePath?.let { path ->
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

    fun onBackPressed() {
        postState = PostState.Content(PostContent.Text())
    }

    companion object {
        const val POST_LENGTH_LIMIT = 256
    }
}
