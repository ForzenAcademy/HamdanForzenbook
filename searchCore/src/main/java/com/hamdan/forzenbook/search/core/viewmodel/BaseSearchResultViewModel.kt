package com.hamdan.forzenbook.search.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    enum class SearchResultType {
        QUERY, ID, NONE
    }

    sealed interface SearchResultState {
        data class Content(
            val posts: List<PostData> = listOf(),
            val type: SearchResultType = SearchResultType.NONE
        ) : SearchResultState

        object Loading : SearchResultState

        object Error : SearchResultState

        object InvalidLogin : SearchResultState
    }

    protected abstract var searchResultState: SearchResultState

    fun getResultsById(
        id: Int,
    ) {
        searchResultState = SearchResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            searchResultState = try {
                SearchResultState.Content(getPostByUserIdUseCase(id), SearchResultType.ID)
            } catch (e: InvalidTokenException) {
                SearchResultState.InvalidLogin
            } catch (e: Exception) {
                SearchResultState.Error
            }
        }
    }

    fun getResultsByQuery(
        query: String,
    ) {
        searchResultState = SearchResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            searchResultState = try {
                SearchResultState.Content(getPostByStringUseCase(query), SearchResultType.QUERY)
            } catch (e: InvalidTokenException) {
                SearchResultState.InvalidLogin
            } catch (e: Exception) {
                SearchResultState.Error
            }
        }
    }

    fun errorDuringSearch() {
        searchResultState = SearchResultState.Error
    }

    fun kickBackToLogin() {
        searchResultState = SearchResultState.InvalidLogin
    }

    fun onErrorDismiss() {
        searchResultState = SearchResultState.Content()
    }
}
