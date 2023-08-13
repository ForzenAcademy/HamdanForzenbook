package com.hamdan.forzenbook.search.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun Search(
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
                true,
                onSearchTextChange,
                onSubmitSearch,
            )
        }

        BaseSearchViewModel.SearchState.Error -> {
            MainContent(
                "",
                false,
                onSearchTextChange,
                onSubmitSearch,
            )
            ForzenbookDialog(
                title = stringResource(id = R.string.generic_error_title),
                body = stringResource(id = R.string.search_error_body),
                confirmationText = stringResource(
                    id = R.string.generic_dialog_confirm
                )
            ) {
                onErrorDismiss()
            }
        }

        BaseSearchViewModel.SearchState.InvalidLogin -> {
            kickBackToLogin()
        }

        else -> throw StateException()
    }
}
