package com.hamdan.forzenbook.mainpage.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composables.FeedBackground
import com.hamdan.forzenbook.compose.core.composables.FeedImagePost
import com.hamdan.forzenbook.compose.core.composables.FeedTextPost
import com.hamdan.forzenbook.compose.core.composables.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composables.PostCard
import com.hamdan.forzenbook.compose.core.composables.TitleText
import com.hamdan.forzenbook.compose.core.composables.UserRow
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.core.GlobalConstants.LOGIN_BASE_URL
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.mainpage.core.viewmodel.BaseFeedViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun FeedPage(
    state: BaseFeedViewModel.FeedState,
    onRequestMorePosts: () -> Unit,
    onNameClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    onCreatePostClicked: () -> Unit,
) {
    when (state) {
        is BaseFeedViewModel.FeedState.Content -> {
            MainContent(
                onCreatePostClicked = onCreatePostClicked,
                posts = state.posts,
                onRequestMorePosts = onRequestMorePosts,
                onNameClick = onNameClick,
                bottomBar = bottomBar
            )
        }

        is BaseFeedViewModel.FeedState.Error -> {
            MainContent(
                onCreatePostClicked = onCreatePostClicked,
                posts = state.posts,
                onRequestMorePosts = onRequestMorePosts,
                onNameClick = onNameClick,
                bottomBar = bottomBar
            )
            // Todo pop open an error dialog, will need to implement when more info is given on the feed page regarding this
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    onCreatePostClicked: () -> Unit,
    posts: List<PostData>,
    onRequestMorePosts: () -> Unit,
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
                        tint = ForzenbookTheme.colors.colors.onBackground,
                        modifier = Modifier
                            .padding(ForzenbookTheme.dimens.grid.x2)
                            .clickable { onCreatePostClicked() },
                    )
                },
            )
        },
        bottomBar = { bottomBar() },
    ) { padding ->
        FeedBackground(
            modifier = Modifier.padding(padding),
        ) {
            itemsIndexed(posts) { index, item ->
                PostCard {
                    UserRow(
                        item.posterIcon,
                        item.posterName,
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
                        FeedImagePost(LOGIN_BASE_URL + item.body)
                    } else {
                        FeedTextPost(item.body)
                    }
                    if (index == posts.size - 1 || posts.isEmpty()) {
                        // Trigger loading more data when the last item is displayed
                        LaunchedEffect(Unit) {
                            onRequestMorePosts()
                        }
                    }
                }
            }
        }
    }
}
