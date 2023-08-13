package com.hamdan.forzenbook.post.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.LoadingOverlay

@Composable
internal fun LoadingContent() {
    BaseContent {
        BackgroundColumn(
            Modifier.padding(it),
            color = MaterialTheme.colorScheme.tertiary
        ) {}
    }
    LoadingOverlay()
}
