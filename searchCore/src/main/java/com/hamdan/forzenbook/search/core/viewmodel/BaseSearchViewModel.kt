package com.hamdan.forzenbook.search.core.viewmodel

import android.content.Context
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
    ){
        searchState.asSearching()?.let { state ->
            viewModelScope.launch {
                try {
                    searchForPostByStringUseCase(state.query)
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
    }

    fun onUpdateSearch(text: String) = updateSearchText(text)

    fun navigateQuery(): String {
        return searchState.asSearching()?.query?.let {
            "/-1/${searchState.asSearching()?.query}/"
        } ?: throw (Exception("Query or state invalid"))
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
}

fun BaseSearchViewModel.SearchState.asSearching(): BaseSearchViewModel.SearchState.Searching? {
    return this as? BaseSearchViewModel.SearchState.Searching
}

// Todo remove these as? extension functions, they are pointless, instead use the if is format
