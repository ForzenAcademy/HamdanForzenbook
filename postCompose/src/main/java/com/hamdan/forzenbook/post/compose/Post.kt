package com.hamdan.forzenbook.post.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.hamdan.forzenbook.compose.core.composables.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composables.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composables.LoadingScreen
import com.hamdan.forzenbook.compose.core.composables.LoginBackgroundColumn
import com.hamdan.forzenbook.compose.core.composables.PillToggleSwitch
import com.hamdan.forzenbook.compose.core.composables.PostTextField
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.post.core.view.PostUiComposeState
import com.hamdan.forzenbook.post.core.viewmodel.toBoolean
import com.hamdan.forzenbook.ui.core.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostContent(
    state: PostUiComposeState,
    onTextChange: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    onToggleClicked: () -> Unit,
    onSendClicked: () -> Unit,
) {
    val navigator = LocalNavController.current
    val text = state.postText
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
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
                    selected = state.postType.toBoolean(),
                ) {
                    onToggleClicked()
                }
            }
        }
    ) { padding ->
        LoginBackgroundColumn(
            modifier = Modifier
                .padding(padding)
                .clickable {
                    keyboard?.show()
                    focusRequester.requestFocus()
                },
        ) {
            PostTextField(text, focusRequester) {
                onTextChange(it)
            }
        }
    }
    if (state.isLoading) {
        LoadingScreen()
    }
    if (state.hasError) {
        ForzenbookDialog(
            title = stringResource(id = R.string.generic_error_title),
            body = stringResource(id = R.string.post_error),
            buttonText = stringResource(id = R.string.generic_dialog_confirm),
        ) {
            onDialogDismiss()
        }
    }
}
