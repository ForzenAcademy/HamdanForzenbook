package com.hamdan.forzenbook.compose.core.composewidgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.hamdan.forzenbook.compose.core.theme.additionalColors
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.GlobalConstants

@Composable
fun ErrorText(
    modifier: Modifier = Modifier,
    error: String,
) {
    Text(
        text = error,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.dimens.grid.x5,
                vertical = MaterialTheme.dimens.grid.x2,
            ),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun LoginTitleSection(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.displayMedium,
        modifier = modifier.padding(MaterialTheme.dimens.grid.x3),
        maxLines = GlobalConstants.ONE_LINE,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
fun TitleText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    style: TextStyle = MaterialTheme.typography.titleLarge
) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = color,
        maxLines = GlobalConstants.ONE_LINE,
        overflow = TextOverflow.Ellipsis,
        style = style,
    )
}

@Composable
fun PostSubtitleText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Text(
        text = text,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall,
        color = textColor,
    )
}

// debatable if this is the correct spot for this
@Composable
fun FeedTextPost(text: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = text)
    }
}

/**
 * intended to be used in large container
 *
 * fills the container with a clickable column
 *
 * inside the column is the text field being typed in
 *
 * requestInitialFocus when true will pop open the keyboard as soon as the field enters the screen
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FullScreenClickableTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String?,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    textFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLabelColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent
    ),
    requestInitialFocus: Boolean = false,
    onTextChange: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    BackgroundColumn(
        modifier = modifier
            .clickable {
                keyboard?.show()
                focusRequester.requestFocus()
            },
        color = backgroundColor,
    ) {
        FocusableTextField(
            text = text,
            label = label,
            focusRequester = focusRequester,
            onValueChange = onTextChange,
            textFieldColors = textFieldColors,
        )
    }
    // pops up the keyboard if true, selects the field as well
    if (requestInitialFocus) {
        LaunchedEffect(Unit) {
            keyboard?.show()
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun FocusableTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String?,
    focusRequester: FocusRequester,
    textFieldColors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        cursorColor = MaterialTheme.colorScheme.onBackground,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedLabelColor = Color.Transparent,
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
    ),
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
        colors = textFieldColors,
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    label: String?,
    value: String,
    padding: PaddingValues = PaddingValues(
        horizontal = MaterialTheme.dimens.grid.x10,
        vertical = MaterialTheme.dimens.grid.x2,
    ),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    onNext: KeyboardActionScope.() -> Unit = {},
    onDone: KeyboardActionScope.() -> Unit = {},
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.additionalColors.inputFieldContainer,
        unfocusedContainerColor = MaterialTheme.additionalColors.inputFieldContainer,
        focusedLabelColor = MaterialTheme.additionalColors.onInputFieldContainer,
        unfocusedTextColor = MaterialTheme.additionalColors.onInputFieldContainer,
        focusedTextColor = MaterialTheme.additionalColors.onInputFieldContainer,
        unfocusedLabelColor = MaterialTheme.additionalColors.onInputFieldContainer,
        focusedBorderColor = MaterialTheme.additionalColors.onInputFieldContainer,
        cursorColor = MaterialTheme.additionalColors.onInputFieldContainer,
    ),
    enabled: Boolean = true,
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        label = if (label != null) {
            {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall
                )
            }
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
        maxLines = GlobalConstants.ONE_LINE,
        enabled = enabled,
        textStyle = textStyle,
    )
}
