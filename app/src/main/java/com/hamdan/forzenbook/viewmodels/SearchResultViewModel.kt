package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCase
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val postById: GetPostByUserIdUseCase,
    private val postByQuery: GetPostByStringUseCase,
) : BaseSearchResultViewModel(
    postByQuery, postById
) {

    private val _state: MutableState<SearchResultState> =
        mutableStateOf(SearchResultState.Content())
    val state: MutableState<SearchResultState>
        get() = _state

    override var searchResultState: SearchResultState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
