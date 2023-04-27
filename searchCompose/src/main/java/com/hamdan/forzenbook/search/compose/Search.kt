package com.hamdan.forzenbook.search.compose

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.hamdan.forzenbook.compose.core.composables.FeedBackground
import com.hamdan.forzenbook.compose.core.composables.InputField
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.typography
import com.hamdan.forzenbook.ui.core.R

@Composable
fun SearchContent() {
    MainContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent() {
    val searchText = remember { mutableStateOf("") }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            InputField(
                label = stringResource(id = R.string.search_label),
                value = searchText.value,
                padding = PaddingValues(
                    vertical = ForzenbookTheme.dimens.grid.x4,
                    horizontal = ForzenbookTheme.dimens.grid.x3,
                ),
                onValueChange = { searchText.value = it },
                textStyle = ForzenbookTheme.typography.bodyLarge,
                imeAction = ImeAction.Done,
                onDone = { Log.v("Hamdan", "Send to results page") },
            )
        },
    ) { padding ->
        FeedBackground(modifier = Modifier.padding(padding), hideLoadIndicator = true) {
        }
    }
}
