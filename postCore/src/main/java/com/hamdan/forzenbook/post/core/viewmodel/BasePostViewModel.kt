package com.hamdan.forzenbook.post.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.GlobalConstants.TOKEN_KEY
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

    fun sendPostClicked(context: Context) {
        when ((postState as PostState.Content).content) {
            is PostContent.Image -> {
                sendImage(context)
            }

            is PostContent.Text -> {
                sendText(context)
            }

            else -> {
                throw Exception("illegal unknown post type")
            }
        }
    }

    fun updateImage(filePath: String?) {
        // in the case the user immediately picks another image, delete the old temp file and create a new one
        postState = PostState.Content(PostContent.Image(filePath))
    }

    private fun sendText(context: Context) {
        val token = context.getSharedPreferences(
            GlobalConstants.TOKEN_PREFERENCE_LOCATION,
            Context.MODE_PRIVATE
        ).getString(TOKEN_KEY, null)
        postState.asTextOrNull()?.let {
            postState = PostState.Loading
            viewModelScope.launch {
                try {
                    if (token == null) throw Exception("invalid token")
                    sendTextPostUseCase(token, it.text)
                    postState = PostState.Content(PostContent.Text())
                } catch (e: Exception) {
                    postState = PostState.Error
                }
            }
        }
    }

    private fun sendImage(context: Context) {
        val token = context.getSharedPreferences(
            GlobalConstants.TOKEN_PREFERENCE_LOCATION,
            Context.MODE_PRIVATE
        ).getString(TOKEN_KEY, null)
        postState.asImageOrNull()?.let {
            it.filePath?.let { path ->
                viewModelScope.launch {
                    try {
                        if (token == null) throw Exception("invalid token")
                        sendImagePostUseCase(token, path)
                        File(path).delete()
                        postState = PostState.Content(PostContent.Image())
                    } catch (e: Exception) {
                        File(path).delete()
                        postState = PostState.Error
                    }
                }
            }
        }
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
