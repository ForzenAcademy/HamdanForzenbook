package com.hamdan.forzenbook.search.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.search.core.domain.SearchForPostByIdUseCase
import com.hamdan.forzenbook.search.core.domain.SearchForPostByStringUseCase
import kotlinx.coroutines.launch

abstract class BaseSearchViewModel(
    private val searchForPostByStringUseCase: SearchForPostByStringUseCase,
    private val searchForPostByIdUseCase: SearchForPostByIdUseCase
) : ViewModel() {

    sealed interface SearchState {
        data class Searching(val query: String = "") : SearchState
        object Error : SearchState
        object InvalidLogin : SearchState
    }

    protected abstract var searchState: SearchState

    fun onNameClicked(
        id: Int,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        viewModelScope.launch {
            try {
                searchForPostByIdUseCase(id)
                onSuccess()
            } catch (e: InvalidTokenException) {
                Log.v("Exception",e.stackTraceToString())
                searchState = SearchState.InvalidLogin
            } catch (e: Exception) {
                Log.v("Exception",e.stackTraceToString())
                onError()
                searchState = SearchState.Error
            }
        }
    }

    fun onSearchSubmit(
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val state = searchState
        if (state is SearchState.Searching) {
            viewModelScope.launch {
                try {
                    searchForPostByStringUseCase(state.query)
                    onSuccess()
                } catch (e: InvalidTokenException) {
                    Log.v("Exception",e.stackTraceToString())
                    searchState = SearchState.InvalidLogin
                } catch (e: Exception) {
                    Log.v("Exception",e.stackTraceToString())
                    onError()
                    searchState = SearchState.Error
                }
            }
        }
    }

    fun onUpdateSearch(text: String){
        searchState = SearchState.Searching(text)
    }

    /**
     * the navigation query to append for navigating to the search result page, intended for string queries
     */
    fun navigationStringQuery(): String {
        val state = searchState
        return if (state is SearchState.Searching) {
            "/-1/${state.query}/"
        } else {
            throw (Exception("Query or state invalid"))
        }
    }

    /**
     * the navigation query to append for navigating to the search result page, intended for id searches
     */
    fun navigationStringUser(id: Int): String = "/$id/%20/"

    fun kickBackToLogin() {
        searchState = SearchState.Searching()
    }

    fun onErrorDismiss() {
        searchState = SearchState.Searching()
    }
}
