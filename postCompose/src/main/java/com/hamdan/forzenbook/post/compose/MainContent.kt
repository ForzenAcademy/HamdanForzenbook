package com.hamdan.forzenbook.post.compose

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.FullScreenClickableTextField
import com.hamdan.forzenbook.compose.core.composewidgets.SubmitButton
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun TextContent(
    state: BasePostViewModel.PostState.Content,
    onTextChange: (String) -> Unit,
    onTogglePressed: () -> Unit,
    onBackPressed: () -> Unit,
    onSendClicked: () -> Unit,
) {
    val showLabel =
        state.content is BasePostViewModel.PostContent.Text && (state.content as BasePostViewModel.PostContent.Text).text.isEmpty()
    val focusManager = LocalFocusManager.current

    BaseContent(
        onTogglePressed = onTogglePressed,
        onBackPressed = onBackPressed,
        onSendClicked = {
            if (!showLabel) {
                onSendClicked()
            }
        },
        onKeyBoardClose = {
            focusManager.clearFocus()
        },
        selected = state.content is BasePostViewModel.PostContent.Text,
    ) { padding ->
        FullScreenClickableTextField(
            modifier = Modifier.padding(padding),
            text = if (state.content is BasePostViewModel.PostContent.Text) (state.content as BasePostViewModel.PostContent.Text).text else "",
            label = if (showLabel) stringResource(id = R.string.post_text_label) else null,
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            onTextChange = onTextChange,
            requestInitialFocus = true,
        )
    }
}

@Composable
internal fun ImageContent(
    state: BasePostViewModel.PostState.Content,
    onTogglePressed: () -> Unit,
    onBackPressed: () -> Unit,
    onSendClicked: () -> Unit,
    onGalleryPressed: () -> Unit,
) {
    val image =
        if (state.content is BasePostViewModel.PostContent.Image) (state.content as BasePostViewModel.PostContent.Image).filePath else null
    BaseContent(
        onTogglePressed = onTogglePressed,
        onSendClicked = {
            if (image != null) {
                onSendClicked()
            }
        },
        onBackPressed = onBackPressed,
        selected = state.content is BasePostViewModel.PostContent.Text,
        additionalBottomContent = {
            if (image != null) {
                SubmitButton(
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimens.grid.x6,
                        vertical = MaterialTheme.dimens.grid.x2
                    ),
                    buttonText = stringResource(id = R.string.post_gallery_change_image),
                    buttonTextStyle = MaterialTheme.typography.titleSmall,
                    enabled = true
                ) {
                    onGalleryPressed()
                }
            }
        }
    ) { padding ->
        BackgroundColumn(
            modifier = Modifier.padding(padding),
            scrollable = false,
            color = MaterialTheme.colorScheme.tertiary,
            arrangement = Arrangement.Center,
        ) {
            if (image == null) {
                SubmitButton(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.grid.x6),
                    buttonText = stringResource(id = R.string.post_gallery_button_text),
                    buttonTextStyle = MaterialTheme.typography.titleSmall,
                    enabled = true
                ) {
                    onGalleryPressed()
                }
            } else {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.grid.x2))
                Image(
                    bitmap = BitmapFactory.decodeFile(image).asImageBitmap(),
                    contentDescription = stringResource(id = R.string.post_send_image),
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.grid.x2)
                        .clip(RoundedCornerShape(MaterialTheme.dimens.grid.x4))
                )
            }
        }
    }
}
