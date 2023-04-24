package com.hamdan.forzenbook.compose.core.composables

import android.graphics.Bitmap
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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.typography
import com.hamdan.forzenbook.ui.core.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val ONE_LINE = 1

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
    label: String,
    value: String,
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
            .padding(
                horizontal = ForzenbookTheme.dimens.grid.x10,
                vertical = ForzenbookTheme.dimens.grid.x2,
            ),
        label = { Text(text = label) },
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
        textStyle = ForzenbookTheme.typography.headlineMedium,
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
    onSubmission: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(horizontal = ForzenbookTheme.dimens.grid.x10)
            .height(ForzenbookTheme.dimens.grid.x20)
            .fillMaxWidth(),
        onClick = {
            onSubmission()
        },
        content = {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = if (enabled) ForzenbookTheme.colors.colors.onPrimary else ForzenbookTheme.colors.colors.onSurface,
                style = ForzenbookTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = ForzenbookTheme.colors.colors.primary,
            disabledContainerColor = ForzenbookTheme.colors.colors.surface,
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
    scrollState: ScrollState = rememberScrollState(),
    color: Color = ForzenbookTheme.colors.colors.background,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color)
            .verticalScroll(scrollState)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForzenbookTopAppBar(
    modifier: Modifier = Modifier,
    topText: String,
    backIcon: Boolean = true,
    actions: @Composable (() -> Unit)? = null,
) {
    val navigator = LocalNavController.current
    TopAppBar(
        title = {
            Text(
                text = topText,
                textAlign = TextAlign.Center,
                color = ForzenbookTheme.colors.colors.onBackground,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = ForzenbookTheme.colors.colors.background),
        navigationIcon = if (backIcon) {
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
            else Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back_arrow),
                tint = Color.Transparent,
            )
        }
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
    disabledColor: Color = ForzenbookTheme.colors.colors.background,
    onToggle: () -> Unit,
) {
    val rowShape = RoundedCornerShape(ForzenbookTheme.dimens.grid.x3)
    Row(
        modifier = modifier
            .clip(rowShape)
            .clickable {
                onToggle()
            }
            .border(ForzenbookTheme.dimens.grid.x1, enabledColor, rowShape),
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
                        vertical = ForzenbookTheme.dimens.grid.x3,
                        horizontal = ForzenbookTheme.dimens.grid.x6
                    )
                    .size(ForzenbookTheme.dimens.imageSizes.medium),
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
                        vertical = ForzenbookTheme.dimens.grid.x3,
                        horizontal = ForzenbookTheme.dimens.grid.x6
                    )
                    .size(ForzenbookTheme.dimens.imageSizes.medium),
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
    val cardShape = RoundedCornerShape(ForzenbookTheme.dimens.grid.x4)
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
fun FeedImagePost(onImageRequestLoad: (String) -> Bitmap, url: String) {
    val cardShape = RoundedCornerShape(ForzenbookTheme.dimens.grid.x4)
    val scope = rememberCoroutineScope()
    val state = remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            state.value = onImageRequestLoad(url)
        }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        state.value?.apply {
            Image(
                bitmap = this.asImageBitmap(),
                stringResource(id = R.string.feed_post_image),
                modifier = Modifier.clip(cardShape)
            )
        } ?: CircularProgressIndicator()
    }
}

@Composable
fun UserRow(
    icon: String? = null,
    name: String,
    location: String,
    date: String,
    onImageRequestLoad: (String) -> Bitmap
) {
    val scope = rememberCoroutineScope()
    val state = remember { mutableStateOf<Bitmap?>(null) }
    if (icon != null) {
        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.IO) {
                state.value = onImageRequestLoad(icon)
            }
        }
    }
    val roundIcon = RoundedCornerShape(ForzenbookTheme.dimens.grid.x20)
    Row(
        modifier = Modifier
            .padding(bottom = ForzenbookTheme.dimens.grid.x2)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if (icon == null) {
            Image(
                modifier = Modifier
                    .size(ForzenbookTheme.dimens.imageSizes.small)
                    .clip(roundIcon),
                painter = painterResource(id = R.drawable.logo_render_full_notext),
                contentDescription = stringResource(id = R.string.lion_icon),
            )
        } else {
            state.value?.apply {
                Image(
                    bitmap = this.asImageBitmap(),
                    stringResource(id = R.string.feed_post_icon),
                    modifier = Modifier
                        .size(ForzenbookTheme.dimens.imageSizes.small)
                        .clip(roundIcon),
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(start = ForzenbookTheme.dimens.grid.x2)
                .weight(1f)
        ) {
            Text(
                text = name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = ForzenbookTheme.typography.bodyLarge,
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
    val cardShape = RoundedCornerShape(ForzenbookTheme.dimens.grid.x4)
    Card(
        modifier = modifier
            .padding(
                horizontal = ForzenbookTheme.dimens.grid.x8,
                vertical = ForzenbookTheme.dimens.grid.x2,
            ),
        shape = cardShape,
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
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier.focusRequester(focusRequester),
        colors = TextFieldDefaults.textFieldColors(
            textColor = ForzenbookTheme.colors.colors.primary,
            containerColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            unfocusedLabelColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = ForzenbookTheme.typography.bodyLarge,
    )
}
