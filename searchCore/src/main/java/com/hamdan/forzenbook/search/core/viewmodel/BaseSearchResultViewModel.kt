package com.hamdan.forzenbook.search.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.core.StateException
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
            val type: SearchResultType = SearchResultType.NONE,
            val loading: Boolean = false,
        ) : SearchResultState

        object Error : SearchResultState

        object InvalidLogin : SearchResultState
    }

    protected abstract var searchResultState: SearchResultState

    fun getResultsById(
        id: Int,
    ) {
        val currentState = searchResultState
        if (currentState is SearchResultState.Content) {
            searchResultState = currentState.copy(loading = true)
            viewModelScope.launch(Dispatchers.IO) {
                searchResultState = try {
                    SearchResultState.Content(
                        getPostByUserIdUseCase(id),
                        SearchResultType.ID,
                        false
                    )
                } catch (e: InvalidTokenException) {
                    Log.v("Exception", e.stackTraceToString())
                    SearchResultState.InvalidLogin
                } catch (e: Exception) {
                    Log.v("Exception", e.stackTraceToString())
                    SearchResultState.Error
                }
            }
        } else throw StateException()
    }

    fun getResultsByQuery(
        query: String,
    ) {
        val currentState = searchResultState
        if (currentState is SearchResultState.Content) {
            searchResultState = currentState.copy(loading = true)
            viewModelScope.launch(Dispatchers.IO) {
                searchResultState = try {
                    SearchResultState.Content(
                        getPostByStringUseCase(query),
                        SearchResultType.QUERY,
                        false
                    )
                } catch (e: InvalidTokenException) {
                    Log.v("Exception", e.stackTraceToString())
                    SearchResultState.InvalidLogin
                } catch (e: Exception) {
                    Log.v("Exception", e.stackTraceToString())
                    SearchResultState.Error
                }
            }
        } else throw StateException()
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
