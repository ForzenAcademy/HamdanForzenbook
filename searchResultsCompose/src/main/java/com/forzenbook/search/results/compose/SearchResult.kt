package com.forzenbook.search.results.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.Divider
import com.hamdan.forzenbook.compose.core.composewidgets.FeedBackground
import com.hamdan.forzenbook.compose.core.composewidgets.FeedImagePost
import com.hamdan.forzenbook.compose.core.composewidgets.FeedTextPost
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composewidgets.PostCard
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.compose.core.composewidgets.UserRow
import com.hamdan.forzenbook.core.GlobalConstants.BASE_URL
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun SearchResultContent(
    state: BaseSearchResultViewModel.SearchResultState,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    onErrorDismiss: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    when (state) {
        is BaseSearchResultViewModel.SearchResultState.Content -> {
            MainContent(
                state = state,
                onIconClick = onIconClick,
                onNameClick = onNameClick,
                bottomBar = bottomBar
            )
        }

        is BaseSearchResultViewModel.SearchResultState.Error -> {
            ErrorContent(bottomBar, onErrorDismiss)
        }

        is BaseSearchResultViewModel.SearchResultState.Loading -> {
            LoadingContent(bottomBar)
        }

        BaseSearchResultViewModel.SearchResultState.InvalidLogin -> {
            kickBackToLogin()
        }

        else -> throw StateException()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StandardContent(
    titleText: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(
                showBackIcon = true,
                titleSection = titleText
            )
        },
        bottomBar = bottomBar
    ) { padding ->
        content(padding)
    }
}

@Composable
private fun ContentBody(
    padding: PaddingValues,
    items: List<PostData>,
    searchType: BaseSearchResultViewModel.SearchResultType,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
) {
    FeedBackground(modifier = Modifier.padding(padding), hideLoadIndicator = true) {
        itemsIndexed(items) { index, item ->
            PostCard {
                UserRow(
                    BASE_URL + item.posterIcon,
                    item.posterFirstName,
                    item.posterLastName,
                    item.posterLocation,
                    item.date,
                    onNameClick = if (searchType == BaseSearchResultViewModel.SearchResultType.QUERY) {
                        { onNameClick(item.posterId) }
                    } else null,
                    onProfileIconClick = { onIconClick(item.posterId) },
                )
                Divider()
                if (item.type == PostData.IMAGE_TYPE) {
                    FeedImagePost(BASE_URL + item.body)
                } else {
                    FeedTextPost(item.body)
                }
            }
        }
    }
}

@Composable
private fun MainContent(
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
            onIconClick = onIconClick,
            onNameClick = onNameClick,
        )
    }
}

@Composable
private fun ErrorContent(
    bottomBar: @Composable () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    StandardContent(
        titleText = {
            Text(stringResource(id = R.string.search_error))
        },
        bottomBar = bottomBar,
    ) {
        FeedBackground(modifier = Modifier.padding(it), hideLoadIndicator = true) {}
    }
    ForzenbookDialog(
        title = stringResource(id = R.string.generic_error_title),
        body = stringResource(id = R.string.search_result_error),
        buttonText = stringResource(id = R.string.generic_dialog_confirm),
    ) {
        onErrorDismiss()
    }
}

@Composable
private fun LoadingContent(
    bottomBar: @Composable () -> Unit,
) {
    StandardContent(
        titleText = {
            Text(
                stringResource(id = R.string.search_result_loading_title),
                color = MaterialTheme.colorScheme.primary
            )
        },
        bottomBar = bottomBar,
    ) {
        FeedBackground(modifier = Modifier.padding(it)) {}
    }
}
