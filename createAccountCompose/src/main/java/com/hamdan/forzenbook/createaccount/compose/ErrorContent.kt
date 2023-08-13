package com.hamdan.forzenbook.createaccount.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun ErrorContent(errorId: Int, onErrorDismiss: () -> Unit) {
    MainContent()
    ForzenbookDialog(
        title = stringResource(R.string.create_account_error_title),
        body = stringResource(errorId),
        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
        onDismiss = onErrorDismiss
    )
}
