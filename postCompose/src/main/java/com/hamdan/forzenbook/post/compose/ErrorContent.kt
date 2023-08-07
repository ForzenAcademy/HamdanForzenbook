package com.hamdan.forzenbook.post.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun ErrorContent(
    onDialogDismiss: () -> Unit,
) {
    BaseContent {
        BackgroundColumn(
            Modifier.padding(it),
            color = MaterialTheme.colorScheme.tertiary
        ) {}
    }
    ForzenbookDialog(
        title = stringResource(id = R.string.generic_error_title),
        body = stringResource(id = R.string.post_error),
        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
    ) {
        onDialogDismiss()
    }
}
