package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.mainpage.core.domain.GetPostsUseCase
import com.hamdan.forzenbook.mainpage.core.viewmodel.BaseFeedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val loadPostsUseCase: GetPostsUseCase
) : BaseFeedViewModel(
    loadPostsUseCase
) {
    private val _state: MutableState<FeedState> = mutableStateOf(FeedState.Content())
    val state: MutableState<FeedState>
        get() = _state

    override var feedState: FeedState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
