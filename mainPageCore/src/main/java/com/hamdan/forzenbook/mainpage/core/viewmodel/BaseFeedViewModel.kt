package com.hamdan.forzenbook.mainpage.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.NullTokenException
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.mainpage.core.domain.GetPostsUseCase
import com.hamdan.forzenbook.ui.core.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFeedViewModel(
    val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    sealed interface FeedState {
        val posts: List<PostData>

        data class Content(override val posts: List<PostData> = listOf()) : FeedState
        data class Error(override val posts: List<PostData> = emptyList()) : FeedState
        data class InvalidLogin(override val posts: List<PostData> = emptyList()) : FeedState
    }

    protected abstract var feedState: FeedState

    fun loadMore(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // this is to simulate loading more posts
            try {
                context.getSharedPreferences(
                    GlobalConstants.TOKEN_PREFERENCE_LOCATION,
                    Context.MODE_PRIVATE
                ).getString(GlobalConstants.TOKEN_KEY, null)?.let {
                    feedState =
                        FeedState.Content(
                            feedState.posts + getPostsUseCase(
                                context.getString(R.string.user_name),
                                it
                            )
                        )
                } ?: throw (NullTokenException())
            } catch (e: Exception) {
                feedState = FeedState.InvalidLogin()
            }
            // Todo adjust this based on how we take the feed from the server in the final implementation
        }
    }

    fun kickBackToLogin() {
        reset()
    }

    private fun reset() {
        feedState = FeedState.Content()
    }
}
