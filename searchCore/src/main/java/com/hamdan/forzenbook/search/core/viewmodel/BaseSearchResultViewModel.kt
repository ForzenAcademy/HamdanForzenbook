package com.hamdan.forzenbook.search.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseSearchResultViewModel(
    private val getPostByStringUseCase: GetPostByStringUseCase,
    private val getPostByUserIdUseCase: GetPostByUserIdUseCase
) : ViewModel() {

    sealed interface SearchResultState {
        val posts: List<PostData>

        data class Content(override val posts: List<PostData> = listOf()) : SearchResultState

        data class Loading(override val posts: List<PostData> = emptyList()) : SearchResultState

        data class Error(override val posts: List<PostData> = emptyList()) : SearchResultState
    }

    protected abstract var searchResultState: SearchResultState

    fun getResultsById(id: Int) {
        searchResultState = SearchResultState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                searchResultState = SearchResultState.Content(getPostByUserIdUseCase(id))
            } catch (e: Exception) {
                searchResultState = SearchResultState.Error()
            }
        }
    }

    fun getResultsByQuery(query: String) {
        searchResultState = SearchResultState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                searchResultState = SearchResultState.Content(getPostByStringUseCase(query))
            } catch (e: Exception) {
                searchResultState = SearchResultState.Error()
            }
        }
    }

    fun errorDuringSearch() {
        searchResultState = SearchResultState.Error()
    }
}

fun BaseSearchResultViewModel.SearchResultState.asContentOrNull(): BaseSearchResultViewModel.SearchResultState.Content? {
    return this as? BaseSearchResultViewModel.SearchResultState.Content
}

fun BaseSearchResultViewModel.SearchResultState.asLoadingOrNull(): BaseSearchResultViewModel.SearchResultState.Loading? {
    return this as? BaseSearchResultViewModel.SearchResultState.Loading
}

fun BaseSearchResultViewModel.SearchResultState.asErrorOrNull(): BaseSearchResultViewModel.SearchResultState.Error? {
    return this as? BaseSearchResultViewModel.SearchResultState.Error
}
