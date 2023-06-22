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
                Log.v("Hamdan", e.message.toString())
                searchState = SearchState.InvalidLogin
            } catch (e: Exception) {
                Log.v("Hamdan", e.message.toString())
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
                    println(e.message.toString())
                    searchState = SearchState.InvalidLogin
                } catch (e: Exception) {
                    println(e.message.toString())
                    onError()
                    searchState = SearchState.Error
                }
            }
        }
    }

    fun onUpdateSearch(text: String) = updateSearchText(text)

    fun navigateQuery(): String {
        val state = searchState
        return if (state is SearchState.Searching) {
            "/-1/${state.query}/"
        } else {
            throw (Exception("Query or state invalid"))
        }
    }

    fun navigateUser(id: Int): String = "/$id/%20/"

    private fun updateSearchText(
        text: String,
    ) {
        searchState = SearchState.Searching(text)
    }

    fun kickBackToLogin() {
        searchState = SearchState.Searching()
    }

    fun onErrorDismiss() {
        searchState = SearchState.Searching()
    }
}
