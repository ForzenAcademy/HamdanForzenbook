package com.hamdan.forzenbook.post.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composewidgets.KeyboardMonitor
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.compose.core.composewidgets.ToggleablePill
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun BaseContent(
    onTogglePressed: () -> Unit = {},
    onSendClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    onKeyBoardClose: () -> Unit = {},
    selected: Boolean = false,
    additionalBottomContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val navController = LocalNavController.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForzenbookTopAppBar(
                titleSection = { TitleText(text = stringResource(R.string.post_top_bar_text)) },
                onBackPressed = {
                    onBackPressed()
                }
            ) {
                Image(
                    painterResource(id = R.drawable.send_post_icon),
                    contentDescription = stringResource(id = R.string.post_send_icon),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier
                        .padding(MaterialTheme.dimens.grid.x3)
                        .clickable { onSendClicked() },
                )
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary)
                    .padding(vertical = MaterialTheme.dimens.grid.x2),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                additionalBottomContent()
                ToggleablePill(
                    imageLeftRes = R.drawable.text_icon,
                    leftDescriptionRes = R.string.text_toggle_text,
                    imageRightRes = R.drawable.image_icon,
                    rightDescriptionRes = R.string.text_toggle_image,
                    selected = selected,
                ) {
                    onTogglePressed()
                }
            }
            Box(modifier = Modifier.height(1.dp)) {
                KeyboardMonitor {
                    if (!it) onKeyBoardClose()
                }
            }
        }
    ) { padding ->
        content(padding)
    }
}
