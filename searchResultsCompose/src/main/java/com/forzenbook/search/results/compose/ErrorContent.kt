package com.forzenbook.search.results.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.FeedBackground
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun ErrorContent(
    bottomBar: @Composable () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    StandardContent(
        titleText = {
            Text(stringResource(id = R.string.search_error))
        },
        bottomBar = bottomBar,
    ) {
        FeedBackground(modifier = Modifier.padding(it), showLoadIndicator = false) {}
    }
    ForzenbookDialog(
        title = stringResource(id = R.string.generic_error_title),
        body = stringResource(id = R.string.search_result_error),
        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
    ) {
        onErrorDismiss()
    }
}
