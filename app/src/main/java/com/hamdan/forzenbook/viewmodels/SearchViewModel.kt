package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCase
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val postById: GetPostByUserIdUseCase,
    private val postByQuery: GetPostByStringUseCase,
) : BaseSearchViewModel(
    postByQuery, postById
) {

    private val _state: MutableState<SearchState> =
        mutableStateOf(SearchState.Content())
    val state: MutableState<SearchState>
        get() = _state

    override var searchState: SearchState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
