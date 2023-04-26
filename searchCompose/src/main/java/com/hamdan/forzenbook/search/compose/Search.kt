package com.hamdan.forzenbook.search.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.hamdan.forzenbook.compose.core.composables.FeedBackground
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
) {
    MainContent(
        state.query,
        onSearchTextChange,
        onSubmitSearch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSubmitSearch: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            InputField(
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
        },
    ) { padding ->
        FeedBackground(modifier = Modifier.padding(padding), hideLoadIndicator = true) {
        }
    }
}
