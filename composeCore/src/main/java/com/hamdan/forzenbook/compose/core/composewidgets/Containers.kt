package com.hamdan.forzenbook.compose.core.composewidgets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.ui.core.R

@Composable
fun ScaffoldWrapper(content: @Composable ColumnScope.() -> Unit) {
    val navController = LocalNavController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(
                titleSection = {
                    TitleText(text = stringResource(R.string.top_bar_text_create_account))
                },
                onBackPressed = {
                    navController?.navigateUp()
                }
            )
        },
    ) { padding ->
        BackgroundColumn(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}
