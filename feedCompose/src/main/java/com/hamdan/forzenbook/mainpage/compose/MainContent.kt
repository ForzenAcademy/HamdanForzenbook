package com.hamdan.forzenbook.mainpage.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.Divider
import com.hamdan.forzenbook.compose.core.composewidgets.FeedBackground
import com.hamdan.forzenbook.compose.core.composewidgets.FeedImagePost
import com.hamdan.forzenbook.compose.core.composewidgets.FeedTextPost
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composewidgets.PostCard
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.compose.core.composewidgets.UserRow
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun MainContent(
    onCreatePostClicked: () -> Unit,
    posts: List<PostData>,
    showLoadIndicator: Boolean,
    onRequestMorePosts: () -> Unit,
    onIconClick: (Int) -> Unit,
    onNameClick: (Int) -> Unit,
    onLogoutPressed: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            ForzenbookTopAppBar(
                titleSection = { TitleText(text = stringResource(id = R.string.feed_top_bar_text)) },
                showBackIcon = true,
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.create_post_button),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(MaterialTheme.dimens.grid.x2)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(bounded = false),
                                onClick = { onCreatePostClicked() }
                            ),
                    )
                },
                onBackPressed = {
                    onLogoutPressed()
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
                        GlobalConstants.BASE_URL + item.posterIcon,
                        item.posterFirstName,
                        item.posterLastName,
                        item.posterLocation,
                        item.date,
                        onNameClick = { onNameClick(item.posterId) },
                        onProfileIconClick = { onIconClick(item.posterId) },
                    )
                    if (item.type == PostData.IMAGE_TYPE) {
                        FeedImagePost(GlobalConstants.BASE_URL + item.body)
                    } else {
                        Divider()
                        FeedTextPost(item.body)
                    }
                    /*
                     Todo if wanting to use a paging style should observe scroll to more appropriately match it
                     The method below will not be good enough in the case that the list size stays the same, use it at your own risk
                    */
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
