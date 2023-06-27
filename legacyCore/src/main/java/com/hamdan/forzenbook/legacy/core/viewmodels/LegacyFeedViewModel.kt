package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.NullTokenException
import com.hamdan.forzenbook.legacy.core.adapters.FeedAdapter
import com.hamdan.forzenbook.legacy.core.adapters.FeedItemViewModel
import com.hamdan.forzenbook.legacy.core.adapters.LoadFeedItemViewModel
import com.hamdan.forzenbook.legacy.core.adapters.toFeedItemViewModel
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.mainpage.core.domain.GetPostsUseCase
import com.hamdan.forzenbook.mainpage.core.viewmodel.BaseFeedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LegacyFeedViewModel @Inject constructor(
    private val postsUseCase: GetPostsUseCase,
    private val navigator: Navigator
) : BaseFeedViewModel(
    postsUseCase
) {
    private val _state: MutableStateFlow<FeedState> = MutableStateFlow(FeedState.Content())
    val state: StateFlow<FeedState>
        get() = _state

    override var feedState: FeedState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    val adapter: FeedAdapter

    init {
        adapter = FeedAdapter(
            getDataAt = {
                if (it == feedState.posts.size) {
                    LoadFeedItemViewModel()
                } else {
                    feedState.posts[it].toFeedItemViewModel()
                }
            },
            getDataSize = {
                if (feedState is FeedState.InvalidLogin || feedState is FeedState.Error) {
                    0 // to avoid the recycler view from trying to load when an error occurs or when kicking the user back to login
                } else {
                    feedState.posts.size + 1
                }
            },
            getDataType = {
                if (it == feedState.posts.size) {
                    FeedItemViewModel.LOADING_MORE
                } else {
                    feedState.posts[it].toFeedItemViewModel().type
                }
            },
            onLoadMore = {
                viewModelScope.launch(Dispatchers.Main) {
                    updateAdapter()
                }
            },
            onNameClick = {
                // Todo when legacy search page is implemented add navigate here
            }
        )
    }

    private fun updateAdapter() {
        viewModelScope.launch(Dispatchers.IO) {
            val initial = feedState.posts.size
            try {
                feedState = FeedState.Content(feedState.posts + getPostsUseCase())
                withContext(Dispatchers.Main) {
                    adapter.notifyItemRangeChanged(initial, feedState.posts.size)
                }
            } catch (e: NullTokenException) {
                feedState = FeedState.InvalidLogin()
            } catch (e: Exception) {
                feedState = FeedState.Error()
            }
        }
    }

    fun newPostClicked(context: Context) {
        navigator.navigateToPost(context)
    }

    /**
     * This version of kick to login must be used instead of the normal kick to login as
     * the legacy navigator is only available in this function and unavailable in the other
     */
    fun legacyKickToLogin(context: Context) {
        navigator.kickToLogin(context)
        feedState =
            FeedState.Content() // to avoid confusion not useing the kickBackToLogin function here even though it is the same thing
    }
}
