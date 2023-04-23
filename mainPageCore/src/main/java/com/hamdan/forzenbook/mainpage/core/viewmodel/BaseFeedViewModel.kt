package com.hamdan.forzenbook.mainpage.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

        data class Content(override val posts: List<PostData>) : FeedState
        data class Error(override val posts: List<PostData>) : FeedState
    }

    protected abstract var feedState: FeedState

    fun loadMore(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // this is to simulate loading more posts
            feedState =
                FeedState.Content(feedState.posts + getPostsUseCase(context.getString(R.string.user_name)))
            // Todo adjust this based on how we take the feed from the server in the final implementation
            // feedState = FeedState.Content(getPostsUseCase())
        }
    }
}
