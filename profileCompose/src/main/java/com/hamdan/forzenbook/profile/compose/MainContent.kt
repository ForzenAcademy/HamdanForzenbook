package com.hamdan.forzenbook.profile.compose

import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel
import com.hamdan.forzenbook.profile.core.viewmodel.ProfileContentSelector

@Composable
internal fun MainContent(
    state: BaseProfileViewModel.ProfileState.Content,
    onBackPressed: () -> Unit,
    onEditPressed: () -> Unit,
    onSelectorPressed: (ProfileContentSelector) -> Unit,
    onBottomReached: () -> Unit,
    onTopReached: () -> Unit,
) {
    state.profileData?.apply {
        BaseContent(
            imagePath = userIconPath,
            firstName = firstName,
            lastName = lastName,
            dateJoined = dateJoined,
            selectorState = bottomState,
            about = aboutUser,
            posts = postSet,
            isOwner = isOwner,
            firstPostId = firstPostId,
            lastPostId = lastPostId,
            onBackPressed = onBackPressed,
            onEditPressed = onEditPressed,
            onSelectorPressed = onSelectorPressed,
            requestScrollUpPosts = onTopReached,
            requestScrollDownPosts = onBottomReached,
        )
    }
}
