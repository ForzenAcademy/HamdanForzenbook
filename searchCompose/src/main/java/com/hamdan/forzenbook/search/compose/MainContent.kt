package com.hamdan.forzenbook.search.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.InputField
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.ui.core.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun MainContent(
    searchText: String,
    requestInitialFocus: Boolean = false,
    onSearchTextChange: (String) -> Unit,
    onSubmitSearch: () -> Unit,
) {
    val navigator = LocalNavController.current
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    BackgroundColumn(color = MaterialTheme.colorScheme.tertiary) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navigator?.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_arrow),
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
            }
            InputField(
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                label = stringResource(id = R.string.search_label),
                value = searchText,
                padding = PaddingValues(
                    vertical = MaterialTheme.dimens.grid.x4,
                    horizontal = MaterialTheme.dimens.grid.x3,
                ),
                onValueChange = onSearchTextChange,
                imeAction = if (searchText.isEmpty()) ImeAction.None else ImeAction.Done,
                onDone = { onSubmitSearch() },
            )
        }
    }
    if (requestInitialFocus) {
        LaunchedEffect(Unit) {
            keyboard?.show()
            focusRequester.requestFocus()
        }
    }
}
