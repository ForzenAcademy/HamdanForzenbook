package com.hamdan.forzenbook.profile.compose

import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.compose.core.composewidgets.LoadingOverlay
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel

@Composable
internal fun LoadingContent(
    state: BaseProfileViewModel.ProfileState.Loading
) {
    state.profileData.apply {
        if (this == null) {
            LoadingOverlay()
        } else {
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
        }
    }
}
