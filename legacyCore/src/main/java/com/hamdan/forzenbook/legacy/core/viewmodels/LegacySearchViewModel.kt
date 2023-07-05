package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.search.core.domain.SearchForPostByIdUseCase
import com.hamdan.forzenbook.search.core.domain.SearchForPostByStringUseCase
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacySearchViewModel @Inject constructor(
    private val postById: SearchForPostByIdUseCase,
    private val postByQuery: SearchForPostByStringUseCase,
    private val navigator: Navigator,
) : BaseSearchViewModel(
    postByQuery, postById,
) {
    private val _state: MutableStateFlow<SearchState> =
        MutableStateFlow(SearchState.Searching())
    val state: StateFlow<SearchState>
        get() = _state

    override var searchState: SearchState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun kickToLogin(context: Context) {
        kickBackToLogin()
        navigator.kickToLogin(context)
    }

    private fun legacyNavigateQuery(): String {
        val tempState = _state.value
        if (tempState is SearchState.Searching) {
            return tempState.query
        }
        throw StateException()
    }

    fun sendToResultSuccessQuery(context: Context) {
        navigator.navigateToSearchResult(context, legacyNavigateQuery(), error = false)
        // navigate with nav query + false boolean to results page
    }

    fun sendToResultFailureQuery(context: Context) {
        navigator.navigateToSearchResult(context, legacyNavigateQuery(), error = true)
        // navigate with nav query + true
    }
}
