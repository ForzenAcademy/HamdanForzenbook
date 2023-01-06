package com.hamdan.forzenbook.view.composeutils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.theme.ForzenbookTheme
import com.hamdan.forzenbook.theme.IconSizeValues
import com.hamdan.forzenbook.theme.PaddingValues
import com.hamdan.forzenbook.theme.TextSizeValues
import com.hamdan.forzenbook.view.LocalNavController

object ComposeUtils {
    const val APP_NAME = "forzenbook"
}

@Composable
fun LoginTitleSection(title: String) {
    Text(
        text = title,
        fontSize = ForzenbookTheme.typography.h1.fontSize,
        modifier = Modifier.padding(PaddingValues.smallPad_3)
    )
}

fun validateEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.None,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNext: KeyboardActionScope.() -> Unit = {},
    onDone: KeyboardActionScope.() -> Unit = {},
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        backgroundColor = ForzenbookTheme.colors.colors.secondaryVariant,
        focusedLabelColor = ForzenbookTheme.colors.colors.onBackground,
        placeholderColor = ForzenbookTheme.colors.colors.onBackground,
        textColor = ForzenbookTheme.colors.colors.onBackground,
        unfocusedLabelColor = ForzenbookTheme.colors.colors.onBackground
    ),
    enabled: Boolean = true,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingValues.largePad_4, vertical = PaddingValues.smallPad_2),
        label = { Text(text = label) },
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = label) },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = onNext,
            onDone = onDone
        ),
        visualTransformation = visualTransformation,
        colors = colors,
        singleLine = true,
        enabled = enabled,
    )
}

@Composable
fun SubmitButton(onSubmission: () -> Unit, label: String, enabled: Boolean) {
    Button(
        modifier = Modifier
            .padding(horizontal = PaddingValues.largePad_4)
            .fillMaxWidth(),
        onClick = {
            onSubmission()
        },
        content = {
            Text(
                text = label,
                modifier = Modifier,
                color = if (enabled) ForzenbookTheme.colors.colors.onPrimary else ForzenbookTheme.colors.colors.onSurface,
                fontSize = TextSizeValues.med_1
            )
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ForzenbookTheme.colors.colors.primary,
            disabledBackgroundColor = ForzenbookTheme.colors.colors.surface
        ),
        enabled = enabled
    )
}

@Composable
fun DimBackgroundLoad() {
    Surface(
        color = Color.Black.copy(alpha = 0.5f),
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ErrorWithIcon(errorText: String, buttonText: String, onSubmission: () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Icon(
            modifier = Modifier.size(IconSizeValues.giga_1),
            tint = ForzenbookTheme.colors.colors.onError,
            painter = painterResource(id = R.drawable.ic_baseline_error_24),
            contentDescription = null
        )
    }
    Text(
        text = errorText,
        textAlign = TextAlign.Center,
        fontSize = TextSizeValues.med_1
    )
    Spacer(modifier = Modifier.height(PaddingValues.medPad_2))
    Button(onClick = {
        onSubmission()
    }
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = PaddingValues.largePad_4, vertical = PaddingValues.medPad_2),
            text = buttonText,
            fontSize = ForzenbookTheme.typography.button.fontSize
        )
    }
}

@Composable
fun LoginBackgroundColumn(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
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
fun LoginTopBar(topText: String) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = PaddingValues.smallPad_3),
        backgroundColor = ForzenbookTheme.colors.colors.background
    ) {
        val navController = LocalNavController.current
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_arrow_back_ios_24),
                contentDescription = "back arrow",
                modifier = Modifier
                    .size(IconSizeValues.small_2)
                    .clickable {
                        navController?.navigateUp()
                    },
                tint = ForzenbookTheme.colors.colors.onBackground
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = topText,
                fontSize = ForzenbookTheme.typography.h3.fontSize,
                textAlign = TextAlign.Center,
                color = ForzenbookTheme.colors.colors.onBackground
            )
        }
        Column(modifier = Modifier.weight(1f)) {
        }
    }
}
