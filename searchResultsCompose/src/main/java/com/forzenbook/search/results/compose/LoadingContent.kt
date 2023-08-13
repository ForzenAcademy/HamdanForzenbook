package com.forzenbook.search.results.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.FeedBackground
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun LoadingContent(
    bottomBar: @Composable () -> Unit,
) {
    StandardContent(
        titleText = {
            Text(
                stringResource(id = R.string.search_result_loading_title),
                color = MaterialTheme.colorScheme.primary
            )
        },
        bottomBar = bottomBar,
    ) {
        FeedBackground(modifier = Modifier.padding(it), showLoadIndicator = true) {}
    }
}
