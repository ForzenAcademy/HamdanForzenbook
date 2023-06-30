package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.legacy.core.adapters.FeedAdapter
import com.hamdan.forzenbook.legacy.core.adapters.toFeedItemViewModel
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.search.core.domain.GetPostByStringUseCase
import com.hamdan.forzenbook.search.core.domain.GetPostByUserIdUseCase
import com.hamdan.forzenbook.search.core.domain.SearchForPostByIdUseCase
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LegacySearchResultViewModel @Inject constructor(
  private val postById: GetPostByUserIdUseCase,
  private val postByQuery: GetPostByStringUseCase,
  private val searchForPostByIdUseCase: SearchForPostByIdUseCase,
  private val navigator: Navigator,
) : BaseSearchResultViewModel(
  postByQuery, postById
) {
  private val _state: MutableStateFlow<SearchResultState> =
    MutableStateFlow(SearchResultState.Content())
  val state: StateFlow<SearchResultState>
    get() = _state


  override var searchResultState: SearchResultState
    get() = state.value
    set(value) {
      _state.value = value
    }

  val adapter: FeedAdapter

  init {
    adapter = FeedAdapter(
      getDataAt = {
        val tempState = searchResultState
        if (tempState is SearchResultState.Content) {
          tempState.posts[it].toFeedItemViewModel()
        } else {
          throw Exception("Illegal state")
        }
      },
      getDataSize = {
        val tempState = searchResultState
        if (tempState is SearchResultState.Content) {
          tempState.posts.size
        } else {
          0
        }
      },
      getDataType = {
        val tempState = searchResultState
        if (tempState is SearchResultState.Content) {
          tempState.posts[it].toFeedItemViewModel().type
        } else {
          throw Exception("Error with feed item")
        }
      },
      onLoadMore = {
        viewModelScope.launch(Dispatchers.Main) {
          // updateAdapter() // this is currently not required
        }
      },
      onNameClick = { context, id ->
        onNameClicked(context, id)
      }
    )
  }

  private fun onNameClicked(
    context: Context,
    id: Int,
  ) {
    viewModelScope.launch {
      try {
        searchForPostByIdUseCase(id)
        navigator.navigateToSearchResult(context, "", id, error = false)
      } catch (e: InvalidTokenException) {
        Log.v("Hamdan", e.message.toString())
        _state.value = SearchResultState.InvalidLogin
      } catch (e: Exception) {
        Log.v("Hamdan", e.message.toString())
        navigator.navigateToSearchResult(context, "", id, error = true)
      }
    }
  }

  fun legacyKickToLogin(context: Context) {
    navigator.kickToLogin(context)
    searchResultState =
      SearchResultState.Content() // to avoid confusion not using the kickBackToLogin function here even though it is the same thing
  }

  fun onSearchClicked(context: Context) {
    navigator.navigateToSearch(context)
  }

  fun onHomeClicked(context: Context) {
    navigator.navigateToFeed(context)
  }

  fun onReceiveId(id: Int) {
    getResultsById(id)
    // ideally you would use a size change for adapter, but in this case it's fine
    // we are only changing the set size once.
    // if in the future we need to change it more than once apply the proper logic, should be found in the feed
    adapter.notifyDataSetChanged()
  }

  fun onReceiveQuery(query: String) {
    getResultsByQuery(query)
    // same as onRecieveId with regards to the notifyDataSetChanged
    adapter.notifyDataSetChanged()
  }

}
