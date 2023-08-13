package com.forzenbook.search.results.compose

import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel

@Composable
fun SearchResult(
    state: BaseSearchResultViewModel.SearchResultState,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    onErrorDismiss: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    when (state) {
        is BaseSearchResultViewModel.SearchResultState.Content -> {
            if (state.loading) {
                LoadingContent(bottomBar)
            } else {
                MainContent(
                    state = state,
                    onIconClick = onIconClick,
                    onNameClick = onNameClick,
                    bottomBar = bottomBar
                )
            }
        }

        is BaseSearchResultViewModel.SearchResultState.Error -> {
            ErrorContent(bottomBar, onErrorDismiss)
        }

        BaseSearchResultViewModel.SearchResultState.InvalidLogin -> {
            kickBackToLogin()
        }

        else -> throw StateException()
    }
}
