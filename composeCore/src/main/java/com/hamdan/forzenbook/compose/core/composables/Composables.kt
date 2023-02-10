package com.hamdan.forzenbook.compose.core.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.ui.core.R

private const val ONE_LINE = 1

@Composable
fun LoginTitleSection(title: String) {
    Text(
        text = title,
        fontSize = ForzenbookTheme.typography.h1.fontSize,
        modifier = Modifier.padding(ForzenbookTheme.dimens.smallPad_2),
        maxLines = ONE_LINE,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onNext: KeyboardActionScope.() -> Unit = {},
    onDone: KeyboardActionScope.() -> Unit = {},
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = ForzenbookTheme.colors.colors.secondaryVariant,
        focusedLabelColor = ForzenbookTheme.colors.colors.onBackground,
        placeholderColor = ForzenbookTheme.colors.colors.onBackground,
        textColor = ForzenbookTheme.colors.colors.onBackground,
        unfocusedLabelColor = ForzenbookTheme.colors.colors.surface,
    ),
    enabled: Boolean = true,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = ForzenbookTheme.dimens.buttonPadHorizontal_2,
                vertical = ForzenbookTheme.dimens.buttonPadVertical_1,
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
        textStyle = TextStyle(fontSize = ForzenbookTheme.typography.h2.fontSize)
    )
}

@Composable
fun ErrorText(error: String) {
    Text(
        text = error,
        fontSize = ForzenbookTheme.typography.h4.fontSize,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(
                horizontal = ForzenbookTheme.dimens.mediumPad_1,
                vertical = ForzenbookTheme.dimens.smallPad_2,
            )
    )
}

@Composable
fun SubmitButton(onSubmission: () -> Unit, label: String, enabled: Boolean) {
    Button(
        modifier = Modifier
            .padding(horizontal = ForzenbookTheme.dimens.buttonPadHorizontal_2)
            .height(ForzenbookTheme.dimens.buttonHeight)
            .fillMaxWidth(),
        onClick = {
            onSubmission()
        },
        content = {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                color = if (enabled) ForzenbookTheme.colors.colors.onPrimary else ForzenbookTheme.colors.colors.onSurface,
                fontSize = ForzenbookTheme.typography.h2.fontSize,
                textAlign = TextAlign.Center,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ForzenbookTheme.colors.colors.primary,
            disabledBackgroundColor = ForzenbookTheme.colors.colors.surface,
        ),
        enabled = enabled
    )
}

@Composable
fun LoadingButton() {
    Button(
        modifier = Modifier
            .padding(horizontal = ForzenbookTheme.dimens.buttonPadHorizontal_2)
            .height(ForzenbookTheme.dimens.buttonHeight)
            .fillMaxWidth(),
        onClick = {},
        content = {
            CircularProgressIndicator(
                color = ForzenbookTheme.colors.colors.onPrimary,
                modifier = Modifier.height(ForzenbookTheme.dimens.largePad_2)
            )
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ForzenbookTheme.colors.colors.primary,
            disabledBackgroundColor = ForzenbookTheme.colors.colors.surface,
        ),
    )
}

@Composable
fun PreventScreenActionsDuringLoad() {
    // put an invisible box on the screen that will take clicks and do nothing
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {},
            ),
    )
}

@Composable
fun LoginBackgroundColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ForzenbookTheme.colors.colors.background)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
    }
}

@Composable
fun LoginTopBar(
    topText: String,
    onNavigateUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = topText,
                fontSize = ForzenbookTheme.typography.h2.fontSize,
                textAlign = TextAlign.Center,
                color = ForzenbookTheme.colors.colors.onBackground,
                maxLines = ONE_LINE,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = ForzenbookTheme.colors.colors.background,
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_arrow),
                    tint = ForzenbookTheme.colors.colors.onBackground,
                )
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back_arrow),
                tint = Color.Transparent,
            )
        }
    )
}

@Composable
fun ForzenbookDialog(title: String, body: String, buttonText: String, onDismiss: () -> Unit) {
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
                        end = ForzenbookTheme.dimens.smallPad_1,
                        bottom = ForzenbookTheme.dimens.smallPad_1
                    )
                    .clickable { onDismiss() },
            )
        },
        modifier = Modifier.padding(ForzenbookTheme.dimens.mediumPad_1)
    )
}
