package com.hamdan.forzenbook.mainpage.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.GlobalConstants.BASE_URL
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.mainpage.core.viewmodel.BaseFeedViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun FeedPage(
    state: BaseFeedViewModel.FeedState,
    onRequestMorePosts: () -> Unit,
    onNameClick: (Int) -> Unit,
    onIconClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    onCreatePostClicked: () -> Unit,
    onErrorDismiss: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    when (state) {
        is BaseFeedViewModel.FeedState.Content -> {
            MainContent(
                onCreatePostClicked = onCreatePostClicked,
                posts = state.posts,
                showLoadIndicator = state.loading,
                onRequestMorePosts = onRequestMorePosts,
                onIconClick = onIconClick,
                onNameClick = onNameClick,
                bottomBar = bottomBar,
            )
        }

        is BaseFeedViewModel.FeedState.Error -> {
            MainContent(
                onCreatePostClicked = onCreatePostClicked,
                posts = state.posts,
                showLoadIndicator = false,
                onRequestMorePosts = onRequestMorePosts,
                onIconClick = onIconClick,
                onNameClick = onNameClick,
                bottomBar = bottomBar,
            )
            ForzenbookDialog(
                title = stringResource(id = R.string.generic_error_title),
                body = stringResource(id = R.string.feed_error_body),
                buttonText = stringResource(id = R.string.generic_dialog_confirm),
            ) {
                onErrorDismiss()
            }
        }

        else -> {
            kickBackToLogin()
        }
    }
}

@Composable
private fun MainContent(
    onCreatePostClicked: () -> Unit,
    posts: List<PostData>,
    showLoadIndicator: Boolean,
    onRequestMorePosts: () -> Unit,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            ForzenbookTopAppBar(
                titleSection = { TitleText(text = stringResource(id = R.string.feed_top_bar_text)) },
                showBackIcon = false,
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.create_post_button),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.grid.x2)
                            .clickable { onCreatePostClicked() },
                    )
                },
            )
        },
        bottomBar = { bottomBar() },
    ) { padding ->
        FeedBackground(
            modifier = Modifier.padding(padding),
            showLoadIndicator = showLoadIndicator,
        ) {
            itemsIndexed(posts) { index, item ->
                PostCard {
                    UserRow(
                        BASE_URL + item.posterIcon,
                        item.posterFirstName,
                        item.posterLastName,
                        item.posterLocation,
                        item.date,
                        onNameClick = { onNameClick(item.posterId) },
                        onProfileIconClick = { onIconClick(item.posterId) },
                    )
                    if (item.type == PostData.IMAGE_TYPE) {
                        FeedImagePost(BASE_URL + item.body)
                    } else {
                        Divider()
                        FeedTextPost(item.body)
                    }
                    // Todo if wanting to use a paging style should observe scroll to more appropriately match it
//                    if (index == posts.size - 1 || posts.isEmpty()) {
//                        // Trigger loading more data when the last item is displayed
//                        LaunchedEffect(Unit) {
//                            onRequestMorePosts()
//                        }
//                    }
                }
            }
        }
    }
}
