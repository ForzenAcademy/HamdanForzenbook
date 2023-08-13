package com.hamdan.forzenbook.profile.compose

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel
import com.hamdan.forzenbook.profile.core.viewmodel.ProfileContentSelector

/**
 * Profile compose page
 * onBackPressed occurs when the user clicks the back icon in the top navigation bar
 * onErrorDismiss occurs when an error dialog is dismissed
 * onEditPressed purpose is for personal profiles, occurs when the user clicks the edit icon in navigation bar
 * onEditAboutPressed when the user is editing their own profile, this occurs when they click the edit icon for the about
 * onAboutTextChanged when the editing about text changes this function is called with the updated text
 * onAboutTextChangeSubmit occurs when the submit button for the edited about modal sheet is pressed
 * onEditProfileImagePressed, when the user is editing their own profile, this occurs when they click the edit icon on the profile image
 * onChangeProfileImagePressed, when the modal sheet for editing the profile image is open, when the image button is pressed this occurs
 * onChangeImageSubmit,  when the modal sheet for editing the profile image is open, when the submit button is pressed this occurs
 * onEditFinishedPressed, when the user presses the finish button in the top navigation bar while editing this occurs
 * onSelectorPressed, occurs when one of the tabs in the selector row is pressed, calls useing the selector for that tab
 * onSheetDismiss, occurs when the editing sheet is dismissed,
 * onBottomReached, occurs when the bottom of the scroll for posts is reached, in current implementation occurs when the bottom button in posts is pressed
 * onTopReached, occurs when the top of the scroll for posts is reached, in current implementation occurs when the top button in posts is pressed
 * kickToLogin, occurs when the user has an invalid login, should kick to the login
 */
@Composable
fun Profile(
    state: BaseProfileViewModel.ProfileState,
    onBackPressed: () -> Unit,
    onErrorDismiss: () -> Unit,
    onEditPressed: () -> Unit,
    onEditAboutPressed: () -> Unit,
    onAboutTextChanged: (String) -> Unit,
    onAboutTextChangeSubmit: () -> Unit,
    onEditProfileImagePressed: () -> Unit,
    onChangeProfileImagePressed: () -> Unit,
    onChangeImageSubmit: () -> Unit,
    onEditFinishedPressed: () -> Unit,
    onSelectorPressed: (ProfileContentSelector) -> Unit,
    onSheetDismiss: () -> Unit,
    onBottomReached: () -> Unit,
    onTopReached: () -> Unit,
    kickToLogin: () -> Unit,
) {

    BackHandler {
        onBackPressed()
    }
    when (state) {
        is BaseProfileViewModel.ProfileState.Content -> {
            MainContent(
                state = state,
                onEditPressed = onEditPressed,
                onBackPressed = onBackPressed,
                onSelectorPressed = onSelectorPressed,
                onBottomReached = onBottomReached,
                onTopReached = onTopReached,
            )
        }

        is BaseProfileViewModel.ProfileState.Error -> {
            ErrorContent(state, onErrorDismiss)
        }

        is BaseProfileViewModel.ProfileState.Loading -> {
            LoadingContent(state = state)
        }

        is BaseProfileViewModel.ProfileState.Editing -> {
            EditingContent(
                state = state,
                onSelectorPressed = onSelectorPressed,
                onAboutTextChanged = onAboutTextChanged,
                onAboutTextChangeSubmit = onAboutTextChangeSubmit,
                onEditImagePressed = onEditProfileImagePressed,
                onChangeImagePressed = onChangeProfileImagePressed,
                onChangeImageSubmit = onChangeImageSubmit,
                onEditSubmitPressed = onEditFinishedPressed,
                onBackPressed = onBackPressed,
                onEditAboutPressed = onEditAboutPressed,
                onSheetDismiss = onSheetDismiss,
                onBottomReached = onBottomReached,
                onTopReached = onTopReached,
            )
        }

        is BaseProfileViewModel.ProfileState.InvalidLogin -> {
            kickToLogin()
        }
    }
}
