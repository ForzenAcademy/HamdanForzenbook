package com.hamdan.forzenbook.post.compose

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composewidgets.FullScreenCickableTextField
import com.hamdan.forzenbook.compose.core.composewidgets.LoadingOverlay
import com.hamdan.forzenbook.compose.core.composewidgets.PillToggleSwitch
import com.hamdan.forzenbook.compose.core.composewidgets.SubmitButton
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel.Companion.POST_LENGTH_LIMIT
import com.hamdan.forzenbook.ui.core.R

@Composable
fun Post(
    state: BasePostViewModel.PostState,
    onTextChange: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    onToggleClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onSendClicked: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    when (state) {
        is BasePostViewModel.PostState.Content -> {
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
                    onToggleClicked = onToggleClicked,
                    onSendClicked = onSendClicked,
                    onGalleryClicked = onGalleryClicked
                )
            }
        }

        BasePostViewModel.PostState.Error -> {
            ErrorContent(onDialogDismiss)
        }

        BasePostViewModel.PostState.Loading -> {
            LoadingContent()
        }

        BasePostViewModel.PostState.InvalidLogin -> {
            kickBackToLogin()
        }

        else -> throw StateException()
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
    val showLabel =
        state is BasePostViewModel.PostState.Content && state.content is BasePostViewModel.PostContent.Text && (state.content as BasePostViewModel.PostContent.Text).text.isEmpty()
    StandardContent(
        onToggleClicked = onToggleClicked,
        onSendClicked = onSendClicked,
        selected = state is BasePostViewModel.PostState.Content && state.content is BasePostViewModel.PostContent.Text
    ) { padding ->
        FullScreenCickableTextField(
            modifier = Modifier.padding(padding),
            text = if (state is BasePostViewModel.PostState.Content && state.content is BasePostViewModel.PostContent.Text) (state.content as BasePostViewModel.PostContent.Text).text else "",
            label = if (showLabel) stringResource(id = R.string.post_text_label) else null,
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            onTextChange = onTextChange
        )
    }
}

@Composable
private fun ImagePostContent(
    state: BasePostViewModel.PostState,
    onToggleClicked: () -> Unit,
    onSendClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
) {
    val image =
        if (state is BasePostViewModel.PostState.Content && state.content is BasePostViewModel.PostContent.Image) (state.content as BasePostViewModel.PostContent.Image).filePath else null
    StandardContent(
        onToggleClicked = onToggleClicked,
        onSendClicked = onSendClicked,
        selected = state is BasePostViewModel.PostState.Content && state.content is BasePostViewModel.PostContent.Text,
        additionalBottomContent = {
            if (image != null) {
                SubmitButton(
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.dimens.grid.x6,
                        vertical = MaterialTheme.dimens.grid.x2
                    ),
                    label = stringResource(id = R.string.post_gallery_change_image),
                    style = MaterialTheme.typography.titleSmall,
                    enabled = true
                ) {
                    onGalleryClicked()
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
                    label = stringResource(id = R.string.post_gallery_button_text),
                    style = MaterialTheme.typography.titleSmall,
                    enabled = true
                ) {
                    onGalleryClicked()
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

@Composable
private fun ErrorContent(
    onDialogDismiss: () -> Unit,
) {
    StandardContent {
        BackgroundColumn(
            Modifier.padding(it),
            color = MaterialTheme.colorScheme.tertiary
        ) {}
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
    StandardContent {
        BackgroundColumn(
            Modifier.padding(it),
            color = MaterialTheme.colorScheme.tertiary
        ) {}
    }
    LoadingOverlay()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StandardContent(
    onToggleClicked: () -> Unit = {},
    onSendClicked: () -> Unit = {},
    selected: Boolean = false,
    additionalBottomContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(titleSection = { TitleText(text = stringResource(R.string.post_top_bar_text)) }) {
                Image(
                    painterResource(id = R.drawable.send_post_icon),
                    contentDescription = stringResource(id = R.string.post_send_icon),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.grid.x3)
                        .clickable { onSendClicked() },
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = MaterialTheme.dimens.grid.x2),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                additionalBottomContent()
                PillToggleSwitch(
                    imageLeftRes = R.drawable.text_icon,
                    leftDescriptionRes = R.string.text_toggle_text,
                    imageRightRes = R.drawable.image_icon,
                    rightDescriptionRes = R.string.text_toggle_image,
                    selected = selected,
                ) {
                    onToggleClicked()
                }
            }
        }
    ) { padding ->
        content(padding)
    }
}
