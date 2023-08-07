package com.hamdan.forzenbook.compose.core.composewidgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.hamdan.forzenbook.compose.core.theme.additionalColors
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.compose.core.theme.staticDimens
import com.hamdan.forzenbook.ui.core.R

@Composable
fun MaterialTheme.cardShape(rounding: Dp = MaterialTheme.dimens.grid.x3): Shape {
    return RoundedCornerShape(rounding)
}

/**
 * Puts a surface in the full container this item is in, consumes any click interactions
 *
 * use content if you want to show anything while loading
 */
@Composable
fun PreventScreenActionsDuringLoad(
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null,
) {
    // put an invisible box on the screen that will take clicks and do nothing
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {},
            ),
        contentAlignment = Alignment.Center,
    ) {
        content?.invoke()
    }
}

/**
 * if put outside of a container, will fill the screen with a surface and load indicator
 */
@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    indicatorColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    PreventScreenActionsDuringLoad {
        CircularProgressIndicator(
            color = indicatorColor,
            modifier = modifier.height(MaterialTheme.dimens.grid.x10),
        )
    }
}

@Composable
fun BackgroundColumn(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    color: Color = MaterialTheme.colorScheme.background,
    arrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color)
            .verticalScroll(scrollState, scrollable)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement,
    ) {
        content()
    }
}

/**
 * custom alert dialog for the app
 *
 * title is not necessary
 *
 *
 */
@Composable
fun ForzenbookDialog(
    modifier: Modifier = Modifier,
    title: String?,
    body: String,
    confirmationText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = if (title == null) {
            null
        } else {
            {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        text = {
            Text(
                text = body,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        confirmButton = {
            Text(
                text = confirmationText,
                modifier = Modifier
                    .padding(
                        end = MaterialTheme.dimens.grid.x2,
                        bottom = MaterialTheme.dimens.grid.x2,
                    )
                    .clickable { onDismiss() },
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier.padding(MaterialTheme.dimens.grid.x5),
    )
}

@Composable
fun FeedBackground(
    modifier: Modifier = Modifier,
    showLoadIndicator: Boolean = false,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        if (showLoadIndicator) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun FeedImagePost(url: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .build(),
            contentDescription = stringResource(id = R.string.feed_post_image),
            modifier = Modifier.clip(MaterialTheme.cardShape(MaterialTheme.dimens.grid.x4)),
            error = {
                Image(
                    modifier = Modifier.clip(MaterialTheme.cardShape(MaterialTheme.dimens.grid.x4)),
                    painter = painterResource(id = R.drawable.logo_render_full_notext),
                    contentDescription = stringResource(id = R.string.lion_icon),
                )
            },
            loading = { CircularProgressIndicator() }
        )
    }
}

/**
 * Intended to be inside of a post card
 *
 * Use this as a heading row or put sufficient padding around it
 */
@Composable
fun UserRow(
    icon: String,
    firstName: String,
    lastName: String,
    location: String,
    date: String,
    onNameClick: (() -> Unit)? = null,
    onProfileIconClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .padding(bottom = MaterialTheme.dimens.grid.x2)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CircularNetworkImage(
            modifier = Modifier.addIf(onProfileIconClick != null) {
                Modifier.clickable { onProfileIconClick?.invoke() }
            },
            imageUrl = icon,
            imageSize = MaterialTheme.dimens.imageSizes.small,
            imageDescription = stringResource(R.string.feed_post_icon)
        )
        Column(
            modifier = Modifier
                .padding(start = MaterialTheme.dimens.grid.x2)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.user_name, firstName, lastName),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.addIf(onNameClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false),
                        onClick = { onNameClick?.invoke() }
                    )
                },
                color = MaterialTheme.colorScheme.onSurface
            )
            PostSubtitleText(text = location)
            PostSubtitleText(text = date)
        }
    }
}

/**
 * Useing Coil, makes a call to the network with the provided URL to load in an image
 *
 * Uses the Forzenbook Logo in the case of an error
 *
 * Shows a circular progress indicator while loading
 */
@Composable
fun CircularNetworkImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    imageSize: Dp = MaterialTheme.dimens.imageSizes.small,
    imageDescription: String,
    onClick: (() -> Unit)? = null,
    borderColor: Color? = null,
    backgroundColor: Color? = MaterialTheme.colorScheme.tertiary,
) {
    val rippleSource = remember { MutableInteractionSource() }
    val ripple = rememberRipple(bounded = true)
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .build(),
        loading = { CircularProgressIndicator() },
        contentDescription = imageDescription,
        modifier = modifier
            .size(imageSize)
            .clip(CircleShape)
            .addIf(onClick != null) {
                Modifier.clickable(
                    interactionSource = rippleSource,
                    indication = ripple,
                    onClick = { onClick?.invoke() }
                )
            }
            .addIf(backgroundColor != null) {
                backgroundColor?.let {
                    return@addIf Modifier.background(backgroundColor)
                } ?: throw Exception()
            }
            .addIf(borderColor != null) {
                Modifier.border(
                    MaterialTheme.dimens.borderGrid.x2,
                    borderColor!!,
                    CircleShape,
                )
            },
        error = {
            Image(
                modifier = modifier
                    .size(imageSize)
                    .clip(CircleShape)
                    .addIf(borderColor != null) {
                        Modifier.border(
                            MaterialTheme.dimens.borderGrid.x2,
                            borderColor!!,
                            CircleShape,
                        )
                    }
                    .addIf(onClick != null) {
                        Modifier.clickable(
                            interactionSource = rippleSource,
                            indication = ripple,
                            onClick = { onClick?.invoke() }
                        )
                    },
                painter = painterResource(id = R.drawable.logo_render_full_notext),
                contentDescription = stringResource(id = R.string.lion_icon),
            )
        },
    )
}

/**
 * Rounded card shape, slightly elevated
 *
 * intended to be used with posts
 */
@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.dimens.grid.x8,
                vertical = MaterialTheme.dimens.grid.x2,
            ),
        shape = MaterialTheme.cardShape(MaterialTheme.dimens.grid.x4),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(MaterialTheme.dimens.grid.x4)
        ) {
            content()
        }
    }
}

/**
 * thin line used as a divider, provide padding if needed
 */
@Composable
fun Divider(
    height: Dp = MaterialTheme.staticDimens.borderGrid.x1,
    padding: Dp = MaterialTheme.dimens.grid.x2,
) {
    Spacer(
        modifier = Modifier
            .padding(padding)
            .background(MaterialTheme.additionalColors.spacerColor)
            .fillMaxWidth()
            .height(height)
    )
}

/**
 * Band-aid fix to find if keyboard is open since no official one is available.
 *
 * Intended to put this in the bottom bar of a scaffold as it stickies to the bottom of the screen.
 *
 * When the bar moves from the keyboard opening we report that the keyboard is open.
 *
 * When the bar moves down to the bottom of the screen we report the keyboard is closed.
 */
@Composable
fun KeyboardMonitor(
    onKeyboardVisibilityChanged: (Boolean) -> Unit,
) {
    var y: Float? by remember { mutableStateOf(null) }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .imePadding()
            .onGloballyPositioned {
                if (y == null) {
                    y = it.positionInWindow().y
                } else {
                    onKeyboardVisibilityChanged(y != it.positionInWindow().y)
                }
            }
    )
}

/**
 * addIf allows you to give a condition requirement, if the condition requirement is valid. the corresponding Modifier will be added
 */
@SuppressLint("UnnecessaryComposedModifier") // needed to pass composable state to [other]
fun Modifier.addIf(condition: Boolean, other: @Composable () -> Modifier): Modifier = composed {
    then(if (condition) other() else Modifier)
}
