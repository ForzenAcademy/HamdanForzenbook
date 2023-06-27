package com.forzenbook.search.results.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composables.FeedBackground
import com.hamdan.forzenbook.compose.core.composables.FeedImagePost
import com.hamdan.forzenbook.compose.core.composables.FeedTextPost
import com.hamdan.forzenbook.compose.core.composables.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composables.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composables.PostCard
import com.hamdan.forzenbook.compose.core.composables.UserRow
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun SearchResultContent(
    state: BaseSearchResultViewModel.SearchResultState,
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    onErrorDismiss: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    when (state) {
        is BaseSearchResultViewModel.SearchResultState.Content -> {
            MainContent(
                state = state,
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

        else -> {
            throw Exception("Illegal unknown state")
        }
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
    onNameClick: (Int) -> Unit,
) {
    FeedBackground(modifier = Modifier.padding(padding), hideLoadIndicator = true) {
        itemsIndexed(items) { index, item ->
            PostCard {
                UserRow(
                    item.posterIcon,
                    item.posterFirstName,
                    item.posterLastName,
                    item.posterLocation,
                    item.date,
                    { onNameClick(item.posterId) },
                )
                Spacer(
                    modifier = Modifier
                        .padding(ForzenbookTheme.dimens.grid.x2)
                        .background(ForzenbookTheme.colors.colors.primary.copy(alpha = .3f))
                        .fillMaxWidth()
                        .height(ForzenbookTheme.dimens.borderGrid.x1)
                )
                if (item.type == PostData.IMAGE_TYPE) {
                    FeedImagePost(GlobalConstants.LOGIN_BASE_URL + item.body)
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
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    StandardContent(
        titleText = {
            Text(
                stringResource(id = R.string.search_result_title_query),
                color = ForzenbookTheme.colors.colors.primary
            )
        },
        bottomBar = bottomBar,
    ) {
        ContentBody(
            padding = it,
            items = state.posts,
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
                color = ForzenbookTheme.colors.colors.primary
            )
        },
        bottomBar = bottomBar,
    ) {
        FeedBackground(modifier = Modifier.padding(it)) {}
    }
}
