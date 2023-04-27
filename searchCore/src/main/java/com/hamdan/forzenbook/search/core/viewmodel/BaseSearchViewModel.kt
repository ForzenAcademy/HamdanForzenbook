package com.hamdan.forzenbook.search.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.search.core.domain.SearchForPostByIdUseCase
import com.hamdan.forzenbook.search.core.domain.SearchForPostByStringUseCase
import kotlinx.coroutines.launch

abstract class BaseSearchViewModel(
    private val searchForPostByStringUseCase: SearchForPostByStringUseCase,
    private val searchForPostByIdUseCase: SearchForPostByIdUseCase
) : ViewModel() {

    sealed interface SearchState {
        val query: String

        data class Searching(override val query: String = "") : SearchState
    }

    protected abstract var searchState: SearchState

    fun onNameClicked(id: Int, onSuccess: () -> Unit, onError: () -> Unit) =
        searchById(id, onSuccess, onError)

    fun onSearchSubmit(onSuccess: () -> Unit, onError: () -> Unit) =
        searchByQuery(onSuccess, onError)

    fun onUpdateSearch(text: String) = updateSearchText(text)

    fun navigateQuery(): String = "/-1/${searchState.query}/"

    fun navigateUser(id: Int): String = "/$id/%20/"

    private fun searchById(id: Int, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                searchForPostByIdUseCase(id)
                onSuccess()
            } catch (e: Exception) {
                Log.v("Hamdan", e.message.toString())
                onError()
            }
        }
    }

    private fun searchByQuery(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                searchForPostByStringUseCase(searchState.query)
                onSuccess()
            } catch (e: Exception) {
                Log.v("Hamdan", e.message.toString())
                onError()
            }
        }
    }

    private fun updateSearchText(text: String) {
        searchState = SearchState.Searching(text)
    }
}
