package com.hamdan.forzenbook.search.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composewidgets.InputField
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun SearchContent(
    state: BaseSearchViewModel.SearchState,
    onSearchTextChange: (String) -> Unit,
    onSubmitSearch: () -> Unit,
    onErrorDismiss: () -> Unit,
    kickBackToLogin: () -> Unit,
) {
    when (state) {
        is BaseSearchViewModel.SearchState.Searching -> {
            MainContent(
                state.query,
                onSearchTextChange,
                onSubmitSearch,
            )
        }

        BaseSearchViewModel.SearchState.Error -> {
            MainContent(
                "",
                onSearchTextChange,
                onSubmitSearch,
            )
            ForzenbookDialog(
                title = stringResource(id = R.string.generic_error_title),
                body = stringResource(id = R.string.search_error_body),
                buttonText = stringResource(
                    id = R.string.generic_dialog_confirm
                )
            ) {
                onErrorDismiss()
            }
        }

        BaseSearchViewModel.SearchState.InvalidLogin -> {
            kickBackToLogin()
        }

        else -> {
            throw Exception("Illegal unknown state")
        }
    }
}

@Composable
fun MainContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSubmitSearch: () -> Unit,
) {
    BackgroundColumn(color = MaterialTheme.colorScheme.tertiary) {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.search_label),
            value = searchText,
            padding = PaddingValues(
                vertical = MaterialTheme.dimens.grid.x4,
                horizontal = MaterialTheme.dimens.grid.x3,
            ),
            onValueChange = onSearchTextChange,
            imeAction = ImeAction.Done,
            onDone = { onSubmitSearch() },
        )
    }
}
