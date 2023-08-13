package com.hamdan.forzenbook.profile.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun ErrorContent(
    state: BaseProfileViewModel.ProfileState.Error,
    onErrorDismiss: () -> Unit,
) {
    val navigator = LocalNavController.current
    state.error.let { error ->
        state.profileData?.apply {
            when (error) {
                is BaseProfileViewModel.ProfileError.Generic -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_body),
                        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }

                BaseProfileViewModel.ProfileError.Loading -> {
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_loading_body),
                        confirmationText = stringResource(id = R.string.generic_dialog_confirm)
                    ) {
                        onErrorDismiss()
                        navigator?.navigateUp()
                    }
                }

                is BaseProfileViewModel.ProfileError.NewAboutMe -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_about_body),
                        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }

                is BaseProfileViewModel.ProfileError.NewIcon -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_icon_body),
                        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }

                is BaseProfileViewModel.ProfileError.Posts -> {
                    BaseContent(
                        imagePath = userIconPath,
                        firstName = firstName,
                        lastName = lastName,
                        dateJoined = dateJoined,
                        selectorState = bottomState,
                        about = aboutUser,
                        firstPostId = firstPostId,
                        lastPostId = lastPostId,
                    )
                    ForzenbookDialog(
                        title = stringResource(id = R.string.generic_error_title),
                        body = stringResource(id = R.string.profile_error_posts_body),
                        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
                        onDismiss = onErrorDismiss
                    )
                }
            }
        }
    }
}