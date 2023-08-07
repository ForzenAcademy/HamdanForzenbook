package com.hamdan.forzenbook.post.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.core.StateException
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
        // if the text isn't larger than the limit and the state is Text
        if (text.length <= POST_LENGTH_LIMIT && currentState is PostState.Content && currentState.content is PostContent.Text) {
            postState = PostState.Content(PostContent.Text(text))
        }
    }

    /**
     * additionalAction is intended for if you want something to happen after a post has finished being sent
     *
     * if no additional action is needed don't add one
     */
    fun sendPostClicked(additionalAction: (() -> Unit)? = null) {
        viewModelScope.launch {
            val succeeded = when ((postState as PostState.Content).content) {
                is PostContent.Image -> {
                    sendImage()
                }

                is PostContent.Text -> {
                    sendText()
                }
            }
            if (succeeded) {
                additionalAction?.invoke()
            }
        }
    }

    fun updateImage(filePath: String?) {
        postState = PostState.Content(PostContent.Image(filePath))
    }

    /**
     * returns false if an error occured
     */
    private suspend fun sendText(): Boolean {
        val state = postState
        if (state is PostState.Content && state.content is PostContent.Text && state.content.text.isNotEmpty() && state.content.text.length <= POST_LENGTH_LIMIT) {
            postState = PostState.Loading
            return try {
                sendTextPostUseCase(state.content.text)
                postState = PostState.Content(PostContent.Text())
                true
            } catch (e: InvalidTokenException) {
                Log.v("Exception", e.stackTraceToString())
                postState = PostState.InvalidLogin
                false
            } catch (e: Exception) {
                Log.v("Exception", e.stackTraceToString())
                postState = PostState.Error
                false
            }
        } else throw StateException()
    }

    /**
     * returns true if an error occured
     */
    private suspend fun sendImage(): Boolean {
        val state = postState
        if (state is PostState.Content && state.content is PostContent.Image) {
            state.content.filePath?.let { path ->
                return try {
                    sendImagePostUseCase(path)
                    File(path).delete()
                    postState = PostState.Content(PostContent.Image())
                    true
                } catch (e: InvalidTokenException) {
                    Log.v("Exception", e.stackTraceToString())
                    File(path).delete()
                    postState = PostState.InvalidLogin
                    false
                } catch (e: Exception) {
                    Log.v("Exception", e.stackTraceToString())
                    File(path).delete()
                    postState = PostState.Error
                    false
                }
            } ?: throw Exception("Image not found")
        } else throw StateException()
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
