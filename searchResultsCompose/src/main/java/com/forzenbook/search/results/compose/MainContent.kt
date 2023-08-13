package com.forzenbook.search.results.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun MainContent(
    state: BaseSearchResultViewModel.SearchResultState.Content,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    StandardContent(
        titleText = {
            TitleText(text = stringResource(id = R.string.search_result_title_query))
        },
        bottomBar = bottomBar,
    ) {
        ContentBody(
            padding = it,
            searchType = state.type,
            items = state.posts,
            showLoadIndicator = state.loading,
            onIconClick = onIconClick,
            onNameClick = onNameClick,
        )
    }
}
