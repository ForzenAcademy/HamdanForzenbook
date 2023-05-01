package com.hamdan.forzenbook.search.core.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.GlobalConstants
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
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) = searchById(id, context, onSuccess, onError)

    fun onSearchSubmit(
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) = searchByQuery(context, onSuccess, onError)

    fun onUpdateSearch(text: String) = updateSearchText(text)

    fun navigateQuery(): String {
        return searchState.asSearching()?.query?.let {
            "/-1/${searchState.asSearching()?.query}/"
        } ?: throw (Exception("Query or state invalid"))
    }

    fun navigateUser(id: Int): String = "/$id/%20/"

    private fun searchById(id: Int, context: Context, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                context.getSharedPreferences(
                    GlobalConstants.TOKEN_PREFERENCE_LOCATION,
                    Context.MODE_PRIVATE
                ).getString(GlobalConstants.TOKEN_KEY, null)?.let {
                    searchForPostByIdUseCase(id, it)
                    onSuccess()
                } ?: throw (InvalidTokenException())
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

    private fun searchByQuery(
        context: Context,
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        searchState.asSearching()?.let { state ->
            viewModelScope.launch {
                try {
                    context.getSharedPreferences(
                        GlobalConstants.TOKEN_PREFERENCE_LOCATION,
                        Context.MODE_PRIVATE
                    ).getString(GlobalConstants.TOKEN_KEY, null)?.apply {
                        searchForPostByStringUseCase(state.query, this@apply)
                        onSuccess()
                    } ?: throw (InvalidTokenException())
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

    private fun updateSearchText(
        text: String,
    ) {
        searchState = SearchState.Searching(text)
    }

    fun kickBackToLogin() {
        reset()
    }

    private fun reset() {
        searchState = SearchState.Searching()
    }
}

fun BaseSearchViewModel.SearchState.asSearching(): BaseSearchViewModel.SearchState.Searching? {
    return this as? BaseSearchViewModel.SearchState.Searching
}
