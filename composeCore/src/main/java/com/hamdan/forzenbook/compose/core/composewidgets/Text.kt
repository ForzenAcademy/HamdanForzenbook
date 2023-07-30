package com.hamdan.forzenbook.compose.core.composewidgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.dimens.grid.x5,
                vertical = MaterialTheme.dimens.grid.x3,
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
fun TitleText(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onPrimary,
        maxLines = GlobalConstants.ONE_LINE,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleLarge
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

// Todo add color choice to parameters
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
            textColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            containerColor = Color.Transparent,
            focusedLabelColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = MaterialTheme.additionalColors.inputFieldContainer,
        focusedLabelColor = MaterialTheme.additionalColors.onInputFieldContainer,
        placeholderColor = MaterialTheme.additionalColors.onInputFieldContainer,
        textColor = MaterialTheme.additionalColors.onInputFieldContainer,
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
