package com.hamdan.forzenbook.search.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseSearchViewModel(
    val getPostByStringUseCase: GetPostByStringUseCase,
    val getPostByUserIdUseCase: GetPostByUserIdUseCase
) : ViewModel() {

    sealed interface SearchState {
        val posts: List<PostData>
        val query: String

        data class Content(override val posts: List<PostData> = listOf(), override val query: String = "") :
            SearchState

        data class Error(override val posts: List<PostData> = listOf(), override val query: String = "") :
            SearchState
    }

    protected abstract var searchState: SearchState

    fun searchById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Todo when integrating with the compose page remove these logs and temporary value
            val temp = getPostByUserIdUseCase(id)
            temp.forEach {
                Log.v("Hamdan", "${it.posterName}, type: ${it.type}, time:${it.date}")
            }
            searchState = SearchState.Content(temp)
        }
    }

    fun searchByQuery(query: String) {
        viewModelScope.launch {
            // Todo when integrating with the compose page remove these logs
            val temp = getPostByStringUseCase(query)
            temp.forEach {
                Log.v("Hamdan", "${it.posterName}, type: ${it.type}, time:${it.date}")
            }
            searchState = SearchState.Content(temp, query)
        }
    }
}
