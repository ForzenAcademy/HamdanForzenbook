package com.hamdan.forzenbook.search.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.InvalidTokenException
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
        data class Content(val posts: List<PostData> = listOf()) : SearchResultState

        object Loading : SearchResultState

        object Error : SearchResultState

        object InvalidLogin : SearchResultState
    }

    protected abstract var searchResultState: SearchResultState

    fun getResultsById(
        id: Int,
        context: Context,
    ) {
        searchResultState = SearchResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.getSharedPreferences(
                    GlobalConstants.TOKEN_PREFERENCE_LOCATION,
                    Context.MODE_PRIVATE
                ).getString(GlobalConstants.TOKEN_KEY, null)?.let {
                    searchResultState =
                        SearchResultState.Content(getPostByUserIdUseCase(id, it))
                } ?: throw (InvalidTokenException())
            } catch (e: InvalidTokenException) {
                searchResultState = SearchResultState.InvalidLogin
            } catch (e: Exception) {
                searchResultState = SearchResultState.Error
            }
        }
    }

    fun getResultsByQuery(
        query: String,
        context: Context,
    ) {
        searchResultState = SearchResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.getSharedPreferences(
                    GlobalConstants.TOKEN_PREFERENCE_LOCATION,
                    Context.MODE_PRIVATE
                ).getString(GlobalConstants.TOKEN_KEY, null)?.let {
                    searchResultState =
                        SearchResultState.Content(getPostByStringUseCase(query, it))
                } ?: throw (InvalidTokenException())
            } catch (e: InvalidTokenException) {
                searchResultState = SearchResultState.InvalidLogin
            } catch (e: Exception) {
                searchResultState = SearchResultState.Error
            }
        }
    }

    fun errorDuringSearch() {
        searchResultState = SearchResultState.Error
    }

    fun kickBackToLogin() {
        reset()
    }

    private fun reset() {
        searchResultState = SearchResultState.Content()
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
