package com.hamdan.forzenbook.mainpage.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.mainpage.core.viewmodel.BaseFeedViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun Feed(
    state: BaseFeedViewModel.FeedState,
    onRequestMorePosts: () -> Unit,
    onNameClick: (Int) -> Unit,
    onIconClick: (Int) -> Unit,
    bottomBar: @Composable () -> Unit,
    onCreatePostClicked: () -> Unit,
    onErrorDismiss: () -> Unit,
    onLogoutPressed: () -> Unit,
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
                onLogoutPressed = onLogoutPressed,
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
                onLogoutPressed = {},
                bottomBar = bottomBar,
            )
            ForzenbookDialog(
                title = stringResource(id = R.string.generic_error_title),
                body = stringResource(id = R.string.feed_error_body),
                confirmationText = stringResource(id = R.string.generic_dialog_confirm),
            ) {
                onErrorDismiss()
            }
        }

        else -> {
            kickBackToLogin()
        }
    }
}
