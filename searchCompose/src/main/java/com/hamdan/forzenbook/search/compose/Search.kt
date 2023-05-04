package com.hamdan.forzenbook.search.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.hamdan.forzenbook.compose.core.composables.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composables.InputField
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.typography
import com.hamdan.forzenbook.search.core.viewmodel.BaseSearchViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
fun SearchContent(
    state: BaseSearchViewModel.SearchState,
    onSearchTextChange: (String) -> Unit,
    onSubmitSearch: () -> Unit,
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
            // Todo show an error dialog with normal search
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
    BackgroundColumn(color = ForzenbookTheme.colors.colors.tertiary) {
        InputField(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = R.string.search_label),
            value = searchText,
            padding = PaddingValues(
                vertical = ForzenbookTheme.dimens.grid.x4,
                horizontal = ForzenbookTheme.dimens.grid.x3,
            ),
            onValueChange = onSearchTextChange,
            textStyle = ForzenbookTheme.typography.bodyLarge,
            imeAction = ImeAction.Done,
            onDone = { onSubmitSearch() },
        )
    }
}
