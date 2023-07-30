package com.hamdan.forzenbook.compose.core.composewidgets

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.compose.core.theme.disabledAlpha
import com.hamdan.forzenbook.compose.core.theme.staticDimens
import com.hamdan.forzenbook.core.GlobalConstants.ONE_LINE

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean,
    style: TextStyle = MaterialTheme.typography.titleMedium,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onSubmission: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimens.grid.x10)
            .fillMaxWidth(),
        onClick = {
            onSubmission()
        },
        content = {
            Text(
                text = label,
                modifier = Modifier
                    .weight(1f)
                    .padding(MaterialTheme.dimens.grid.x2),
                color = textColor,
                textAlign = TextAlign.Center,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis,
                style = style,
                fontWeight = FontWeight.Bold
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = containerColor,
            disabledContainerColor = containerColor.copy(alpha = MaterialTheme.disabledAlpha),
            disabledContentColor = containerColor.copy(alpha = MaterialTheme.disabledAlpha),
        ),
        enabled = enabled,
    )
}

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .padding(horizontal = MaterialTheme.dimens.grid.x10)
            .fillMaxWidth(),
        onClick = {},
        content = {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    )
}

@Composable
fun PillToggleSwitch(
    modifier: Modifier = Modifier,
    @DrawableRes imageLeftRes: Int,
    @StringRes leftDescriptionRes: Int,
    @DrawableRes imageRightRes: Int,
    @StringRes rightDescriptionRes: Int,
    // false is the default, indicates left is the selected item
    selected: Boolean = false,
    enabledColor: Color = MaterialTheme.colorScheme.onBackground,
    disabledColor: Color = MaterialTheme.colorScheme.background,
    onToggle: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.cardShape())
            .clickable {
                onToggle()
            }
            .border(MaterialTheme.dimens.borderGrid.x2, enabledColor, MaterialTheme.cardShape()),
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = MaterialTheme.dimens.grid.x3,
                        bottomStart = MaterialTheme.dimens.grid.x3
                    )
                )
                .background(if (selected) enabledColor else disabledColor),
        ) {
            Image(
                painterResource(id = imageLeftRes),
                contentDescription = stringResource(id = leftDescriptionRes),
                colorFilter = ColorFilter.tint(if (selected) disabledColor else enabledColor),
                modifier = Modifier
                    .padding(
                        vertical = MaterialTheme.dimens.grid.x2,
                        horizontal = MaterialTheme.dimens.grid.x6
                    )
                    .size(MaterialTheme.staticDimens.imageSizes.tiny),
            )
        }
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topEnd = MaterialTheme.dimens.grid.x3,
                        bottomEnd = MaterialTheme.dimens.grid.x3
                    )
                )
                .background(if (!selected) enabledColor else disabledColor),
        ) {
            Image(
                painterResource(id = imageRightRes),
                contentDescription = stringResource(id = rightDescriptionRes),
                colorFilter = ColorFilter.tint(if (!selected) disabledColor else enabledColor),
                modifier = Modifier
                    .padding(
                        vertical = MaterialTheme.dimens.grid.x2,
                        horizontal = MaterialTheme.dimens.grid.x6
                    )
                    .size(MaterialTheme.staticDimens.imageSizes.tiny),
            )
        }
    }
}
