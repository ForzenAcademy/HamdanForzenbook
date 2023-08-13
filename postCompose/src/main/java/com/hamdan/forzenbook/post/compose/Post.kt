package com.hamdan.forzenbook.post.compose

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.post.core.viewmodel.BasePostViewModel

@Composable
fun Post(
    state: BasePostViewModel.PostState,
    onTextChange: (String) -> Unit,
    onDialogDismiss: () -> Unit,
    onTogglePressed: () -> Unit,
    onBackPressed: () -> Unit,
    onGalleryPressed: () -> Unit,
    onSendClicked: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    BackHandler {
        onBackPressed()
    }
    when (state) {
        is BasePostViewModel.PostState.Content -> {
            if (state.content is BasePostViewModel.PostContent.Text) {
                TextContent(
                    state = state,
                    onTextChange = onTextChange,
                    onTogglePressed = onTogglePressed,
                    onBackPressed = onBackPressed,
                    onSendClicked = onSendClicked
                )
            } else {
                ImageContent(
                    state = state,
                    onTogglePressed = onTogglePressed,
                    onSendClicked = onSendClicked,
                    onGalleryPressed = onGalleryPressed,
                    onBackPressed = onBackPressed,
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
