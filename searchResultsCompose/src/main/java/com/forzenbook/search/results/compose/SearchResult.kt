package com.forzenbook.search.results.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composables.FeedBackground
import com.hamdan.forzenbook.compose.core.composables.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.ui.core.R

@Composable
fun SearchResultContent(
    bottomBar: @Composable () -> Unit,
) {
    MainContent(
        bottomBar,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    bottomBar: @Composable () -> Unit,
) {
    // these are just for temporary testing
    val searchType = true
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(
                showBackIcon = true,
                titleSection = {
                    Text(
                        stringResource(
                            if (searchType)
                                R.string.search_title_query
                            else
                                R.string.search_title_query
                        ),
                        color = ForzenbookTheme.colors.colors.primary
                    )
                }
            )
        },
        bottomBar = bottomBar
    ) { padding ->
        FeedBackground(modifier = Modifier.padding(padding), hideLoadIndicator = true) {
        }
    }
}
