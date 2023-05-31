package com.hamdan.forzenbook.compose.core.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.typography
import com.hamdan.forzenbook.compose.core.theme.staticDimens
import com.hamdan.forzenbook.core.NavigationItem
import com.hamdan.forzenbook.ui.core.R

private const val ONE_LINE = 1

@Composable
fun MaterialTheme.cardShape(rounding: Dp = ForzenbookTheme.dimens.grid.x3): Shape {
    return RoundedCornerShape(rounding)
}

@Composable
fun LoginTitleSection(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
        style = ForzenbookTheme.typography.titleLarge,
        modifier = modifier.padding(ForzenbookTheme.dimens.grid.x3),
        maxLines = ONE_LINE,
        overflow = TextOverflow.Ellipsis,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: String?,
    value: String,
    padding: PaddingValues = PaddingValues(
        horizontal = ForzenbookTheme.dimens.grid.x10,
        vertical = ForzenbookTheme.dimens.grid.x2,
    ),
    textStyle: TextStyle = ForzenbookTheme.typography.headlineMedium,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onNext: KeyboardActionScope.() -> Unit = {},
    onDone: KeyboardActionScope.() -> Unit = {},
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = ForzenbookTheme.colors.colors.onSurface,
        focusedLabelColor = ForzenbookTheme.colors.colors.onBackground,
        placeholderColor = ForzenbookTheme.colors.colors.onBackground,
        textColor = ForzenbookTheme.colors.colors.onBackground,
        unfocusedLabelColor = ForzenbookTheme.colors.colors.surface,
    ),
    enabled: Boolean = true,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        label = if (label != null) {
            { Text(text = label) }
        } else null,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(
            onNext = onNext,
            onDone = onDone,
        ),
        visualTransformation = visualTransformation,
        colors = colors,
        maxLines = ONE_LINE,
        enabled = enabled,
        textStyle = textStyle,
    )
}

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    error: String,
) {
    Text(
        text = error,
        style = ForzenbookTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(
                horizontal = ForzenbookTheme.dimens.grid.x5,
                vertical = ForzenbookTheme.dimens.grid.x3,
            ),
    )
}

@Composable
fun SubmitButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean,
    style: TextStyle = ForzenbookTheme.typography.titleMedium,
    onSubmission: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(horizontal = ForzenbookTheme.dimens.grid.x10)
            .fillMaxWidth(),
        onClick = {
            onSubmission()
        },
        content = {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = if (enabled) ForzenbookTheme.colors.colors.onPrimary else ForzenbookTheme.colors.colors.onSurface,
                textAlign = TextAlign.Center,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis,
                style = style,
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = ForzenbookTheme.colors.colors.primary,
            disabledContainerColor = ForzenbookTheme.colors.colors.surface,
        ),
    )
}

@Composable
fun LoadingButton(
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier
            .padding(horizontal = ForzenbookTheme.dimens.grid.x10)
            .height(ForzenbookTheme.dimens.grid.x20)
            .fillMaxWidth(),
        onClick = {},
        content = {
            CircularProgressIndicator(
                color = ForzenbookTheme.colors.colors.onPrimary,
                modifier = Modifier.height(ForzenbookTheme.dimens.grid.x10),
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = ForzenbookTheme.colors.colors.primary,
            disabledContainerColor = ForzenbookTheme.colors.colors.surface,
        ),
    )
}

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

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    indicatorColor: Color = ForzenbookTheme.colors.colors.primary,
) {
    PreventScreenActionsDuringLoad {
        CircularProgressIndicator(
            color = indicatorColor,
            modifier = modifier.height(ForzenbookTheme.dimens.grid.x10),
        )
    }
}

@Composable
fun BackgroundColumn(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    scrollState: ScrollState = rememberScrollState(),
    color: Color = ForzenbookTheme.colors.colors.background,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForzenbookTopAppBar(
    modifier: Modifier = Modifier,
    showBackIcon: Boolean = true,
    titleSection: @Composable () -> Unit,
    actions: @Composable (() -> Unit)? = null,
) {
    val navigator = LocalNavController.current
    TopAppBar(
        title = {
            titleSection()
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(ForzenbookTheme.dimens.borderGrid.x2),
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = ForzenbookTheme.colors.colors.background),
        navigationIcon = if (showBackIcon) {
            {
                IconButton(onClick = { navigator?.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_arrow),
                        tint = ForzenbookTheme.colors.colors.onBackground,
                    )
                }
            }
        } else {
            {}
        },
        actions = {
            if (actions != null) actions()
        }
    )
}

@Composable
fun ForzenbookBottomNavigationBar(navIcons: List<NavigationItem>) {
    val navigator = LocalNavController.current
    NavigationBar(containerColor = ForzenbookTheme.colors.colors.background) {
        navIcons.forEach {
            ForzenbookNavigationItem(
                label = it.label,
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        contentDescription = stringResource(it.description),
                    )
                },
                onClick = {
                    navigator?.navigate(it.page) {
                        popUpTo(it.page)
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}

@Composable
fun RowScope.ForzenbookNavigationItem(
    label: Int,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(ForzenbookTheme.dimens.grid.x2)
            .weight(1f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .padding(ForzenbookTheme.dimens.grid.x2)
                .fillMaxSize()
                .clickable {
                    onClick()
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            icon()
            Text(text = stringResource(id = label))
        }
    }
}

@Composable
fun TitleText(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = ForzenbookTheme.colors.colors.onBackground,
        maxLines = ONE_LINE,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun ForzenbookDialog(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    buttonText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = body)
        },
        confirmButton = {
            Text(
                text = buttonText,
                modifier = Modifier
                    .padding(
                        end = ForzenbookTheme.dimens.grid.x2,
                        bottom = ForzenbookTheme.dimens.grid.x2,
                    )
                    .clickable { onDismiss() },
                color = ForzenbookTheme.colors.colors.onPrimary,
            )
        },
        modifier = modifier.padding(ForzenbookTheme.dimens.grid.x5),
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
    enabledColor: Color = ForzenbookTheme.colors.colors.primary,
    disabledColor: Color = ForzenbookTheme.colors.colors.tertiary,
    onToggle: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.cardShape())
            .clickable {
                onToggle()
            }
            .border(ForzenbookTheme.dimens.borderGrid.x2, enabledColor, MaterialTheme.cardShape()),
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = ForzenbookTheme.dimens.grid.x3,
                        bottomStart = ForzenbookTheme.dimens.grid.x3
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
                        vertical = ForzenbookTheme.dimens.grid.x2,
                        horizontal = ForzenbookTheme.dimens.grid.x6
                    )
                    .size(MaterialTheme.staticDimens.imageSizes.tiny),
            )
        }
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topEnd = ForzenbookTheme.dimens.grid.x3,
                        bottomEnd = ForzenbookTheme.dimens.grid.x3
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
                        vertical = ForzenbookTheme.dimens.grid.x2,
                        horizontal = ForzenbookTheme.dimens.grid.x6
                    )
                    .size(MaterialTheme.staticDimens.imageSizes.tiny),
            )
        }
    }
}

@Composable
fun FeedBackground(
    modifier: Modifier = Modifier,
    hideLoadIndicator: Boolean = false,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ForzenbookTheme.colors.colors.tertiary)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        if (!hideLoadIndicator) {
            item {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun FeedTextPost(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = text)
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
            modifier = Modifier.clip(MaterialTheme.cardShape(ForzenbookTheme.dimens.grid.x4)),
            error = {
                Image(
                    modifier = Modifier.clip(MaterialTheme.cardShape(ForzenbookTheme.dimens.grid.x4)),
                    painter = painterResource(id = R.drawable.logo_render_full_notext),
                    contentDescription = stringResource(id = R.string.lion_icon),
                )
            },
            loading = { CircularProgressIndicator() }
        )
    }
}

@Composable
fun UserRow(
    icon: String? = null,
    firstName: String,
    lastName: String,
    location: String,
    date: String,
    onNameClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(bottom = ForzenbookTheme.dimens.grid.x2)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(icon)
                .build(),
            placeholder = painterResource(id = R.drawable.logo_render_full_notext),
            contentDescription = stringResource(id = R.string.feed_post_icon),
            modifier = Modifier
                .size(ForzenbookTheme.dimens.imageSizes.small)
                .clip(MaterialTheme.cardShape(ForzenbookTheme.dimens.grid.x20)),
            error = painterResource(id = R.drawable.logo_render_full_notext),
        )
        Column(
            modifier = Modifier
                .padding(start = ForzenbookTheme.dimens.grid.x2)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.user_name, firstName, lastName),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = ForzenbookTheme.typography.bodyLarge,
                modifier = Modifier.clickable { onNameClick() }
            )
            Text(
                text = location,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = ForzenbookTheme.typography.bodySmall,
                color = ForzenbookTheme.colors.colors.surface,
            )
            Text(
                text = date,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = ForzenbookTheme.typography.bodySmall,
                color = ForzenbookTheme.colors.colors.surface,
            )
        }
    }
}

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .padding(
                horizontal = ForzenbookTheme.dimens.grid.x8,
                vertical = ForzenbookTheme.dimens.grid.x2,
            ),
        shape = MaterialTheme.cardShape(ForzenbookTheme.dimens.grid.x4),
        colors = CardDefaults.cardColors(containerColor = ForzenbookTheme.colors.colors.onSurface),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(ForzenbookTheme.dimens.grid.x4)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String?,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        label = {
            if (label != null) {
                Text(text = label)
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = ForzenbookTheme.colors.colors.primary,
            containerColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = ForzenbookTheme.typography.bodyLarge,
    )
}
