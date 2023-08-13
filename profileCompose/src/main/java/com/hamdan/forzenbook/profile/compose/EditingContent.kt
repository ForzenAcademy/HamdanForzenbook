package com.hamdan.forzenbook.profile.compose

import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel
import com.hamdan.forzenbook.profile.core.viewmodel.ProfileContentSelector

@Composable
internal fun EditingContent(
    state: BaseProfileViewModel.ProfileState.Editing,
    onBackPressed: () -> Unit,
    onSelectorPressed: (ProfileContentSelector) -> Unit,
    onEditAboutPressed: () -> Unit,
    onAboutTextChanged: (String) -> Unit,
    onAboutTextChangeSubmit: () -> Unit,
    onEditImagePressed: () -> Unit,
    onChangeImagePressed: () -> Unit,
    onChangeImageSubmit: () -> Unit,
    onEditSubmitPressed: () -> Unit,
    onSheetDismiss: () -> Unit,
    onBottomReached: () -> Unit,
    onTopReached: () -> Unit,
) {
    state.profileData?.apply {
        BaseContent(
            imagePath = userIconPath,
            firstName = firstName,
            lastName = lastName,
            dateJoined = dateJoined,
            posts = postSet,
            selectorState = bottomState,
            about = aboutUser,
            firstPostId = firstPostId,
            lastPostId = lastPostId,
            editingContent = state.editContent,
            onBackPressed = onBackPressed,
            onSelectorPressed = onSelectorPressed,
            onNormalViewPressed = onEditSubmitPressed,
            onEditAboutPressed = onEditAboutPressed,
            onAboutTextChanged = onAboutTextChanged,
            onAboutTextChangeSubmit = onAboutTextChangeSubmit,
            onEditImagePressed = onEditImagePressed,
            onChangeImagePressed = onChangeImagePressed,
            onChangeImageSubmit = onChangeImageSubmit,
            onSheetDismiss = onSheetDismiss,
            requestScrollDownPosts = onBottomReached,
            requestScrollUpPosts = onTopReached,
        )
    }
}
