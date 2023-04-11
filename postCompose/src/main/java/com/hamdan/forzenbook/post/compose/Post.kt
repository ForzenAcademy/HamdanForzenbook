package com.hamdan.forzenbook.post.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composables.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composables.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composables.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composables.LoadingOverlay
import com.hamdan.forzenbook.compose.core.composables.PillToggleSwitch
import com.hamdan.forzenbook.compose.core.composables.PostTextField
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel
import com.hamdan.forzenbook.post.core.viewmodel.getText
import com.hamdan.forzenbook.ui.core.R

@Composable
fun Post(
    state: BasePostViewModel.PostState,
    onTextChange: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    onToggleClicked: () -> Unit,
    onSendClicked: () -> Unit,
) {
    when (true) {
        (state is BasePostViewModel.PostState.Content) -> {
            if (state.content is BasePostViewModel.PostContent.Text) {
                TextPostContent(
                    state = state,
                    onTextChange = onTextChange,
                    onToggleClicked = onToggleClicked
                ) {
                    onSendClicked()
                }
            } else {
                ImagePostContent(
                    state = state,
                    onToggleClicked = onToggleClicked
                ) {
                    onSendClicked()
                }
            }
        }
        (state is BasePostViewModel.PostState.Loading) -> {
            LoadingContent()
        }
        (state is BasePostViewModel.PostState.Error) -> {
            ErrorContent(state, onDialogDismiss)
        }
        else -> {
            throw Exception("Illegal unknown state")
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TextPostContent(
    state: BasePostViewModel.PostState,
    onTextChange: (String) -> Unit,
    onToggleClicked: () -> Unit,
    onSendClicked: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    StandardContent(
        state = state,
        onToggleClicked = onToggleClicked,
        onSendClicked = onSendClicked
    ) { padding ->
        BackgroundColumn(
            modifier = Modifier
                .padding(padding)
                .clickable {
                    keyboard?.show()
                    focusRequester.requestFocus()
                },
        ) {
            PostTextField(
                state.getText(),
                focusRequester
            ) {
                onTextChange(it)
            }
        }
    }
}

@Composable
private fun ImagePostContent(
    state: BasePostViewModel.PostState,
    onToggleClicked: () -> Unit,
    onSendClicked: () -> Unit,
) {
    StandardContent(
        state = state,
        onToggleClicked = onToggleClicked,
        onSendClicked = onSendClicked
    ) { padding ->
        BackgroundColumn(
            modifier = Modifier
                .padding(padding),
        ) {
        }
    }
}

@Composable
private fun ErrorContent(
    state: BasePostViewModel.PostState,
    onDialogDismiss: () -> Unit,
) {
    StandardContent(state = state, onSendClicked = {}, onToggleClicked = {}) {
        BackgroundColumn(Modifier.padding(it)) {}
    }
    ForzenbookDialog(
        title = stringResource(id = R.string.generic_error_title),
        body = stringResource(id = R.string.post_error),
        buttonText = stringResource(id = R.string.generic_dialog_confirm),
    ) {
        onDialogDismiss()
    }
}

@Composable
private fun LoadingContent() {
    LoadingOverlay()
}

@Composable
private fun StandardContent(
    state: BasePostViewModel.PostState,
    onToggleClicked: () -> Unit,
    onSendClicked: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navigator = LocalNavController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(topText = stringResource(R.string.post_top_bar_text)) {
                Image(
                    painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = stringResource(id = R.string.post_send_icon),
                    colorFilter = ColorFilter.tint(ForzenbookTheme.colors.colors.primary),
                    modifier = Modifier
                        .padding(ForzenbookTheme.dimens.grid.x3)
                        .size(ForzenbookTheme.dimens.imageSizes.medium)
                        .clickable { onSendClicked() },
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = ForzenbookTheme.dimens.grid.x2),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                PillToggleSwitch(
                    imageLeftRes = R.drawable.baseline_text_fields_24,
                    leftDescriptionRes = R.string.text_toggle_text,
                    imageRightRes = R.drawable.baseline_image_24,
                    rightDescriptionRes = R.string.text_toggle_image,
                    selected = (state as BasePostViewModel.PostState.Content).content is BasePostViewModel.PostContent.Text,
                ) {
                    onToggleClicked()
                }
            }
        }
    ) { padding ->
        content(padding)
    }
}
