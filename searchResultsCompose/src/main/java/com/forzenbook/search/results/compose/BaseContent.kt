package com.forzenbook.search.results.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.Divider
import com.hamdan.forzenbook.compose.core.composewidgets.FeedBackground
import com.hamdan.forzenbook.compose.core.composewidgets.FeedImagePost
import com.hamdan.forzenbook.compose.core.composewidgets.FeedTextPost
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composewidgets.PostCard
import com.hamdan.forzenbook.compose.core.composewidgets.UserRow
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchResultViewModel

@Composable
internal fun StandardContent(
    titleText: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val navController = LocalNavController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(
                showBackIcon = true,
                titleSection = titleText,
                onBackPressed = {
                    navController?.navigateUp()
                }
            )
        },
        bottomBar = bottomBar
    ) { padding ->
        content(padding)
    }
}

@Composable
internal fun ContentBody(
    padding: PaddingValues,
    items: List<PostData>,
    showLoadIndicator: Boolean,
    searchType: BaseSearchResultViewModel.SearchResultType,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
) {
    FeedBackground(modifier = Modifier.padding(padding), showLoadIndicator = showLoadIndicator) {
        itemsIndexed(items) { _, item ->
            PostCard {
                UserRow(
                    GlobalConstants.BASE_URL + item.posterIcon,
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
                    FeedImagePost(GlobalConstants.BASE_URL + item.body)
                } else {
                    FeedTextPost(item.body)
                }
            }
        }
    }
}
