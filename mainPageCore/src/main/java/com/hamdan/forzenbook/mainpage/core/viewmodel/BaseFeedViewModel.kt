package com.hamdan.forzenbook.mainpage.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.mainpage.core.domain.GetPostsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseFeedViewModel(
    val getPostsUseCase: GetPostsUseCase
) : ViewModel() {

    sealed interface FeedState {
        val posts: List<PostData>

        data class Content(
            override val posts: List<PostData> = listOf(),
            val loading: Boolean = false
        ) :
            FeedState

        data class Error(override val posts: List<PostData> = emptyList()) : FeedState
        data class InvalidLogin(override val posts: List<PostData> = emptyList()) : FeedState
    }

    protected abstract var feedState: FeedState

    fun loadMore() {
        val currentState = feedState
        viewModelScope.launch(Dispatchers.IO) {
            // this is to simulate loading more posts
            try {
                if (currentState is FeedState.Content) {
                    feedState = currentState.copy(loading = true)
                    feedState = FeedState.Content(getPostsUseCase(), false)
                } else throw StateException()
            } catch (e: Exception) {
                feedState = FeedState.InvalidLogin()
            }
        }
    }

    fun kickBackToLogin() {
        feedState = FeedState.Content()
    }

    fun onErrorDismiss() {
        val currentState = feedState
        if (currentState is FeedState.Error) {
            feedState = FeedState.Content()
        } else throw StateException()
    }
}
