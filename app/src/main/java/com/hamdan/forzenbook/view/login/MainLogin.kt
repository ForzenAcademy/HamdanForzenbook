package com.hamdan.forzenbook.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.theme.ForzenbookTheme
import com.hamdan.forzenbook.view.LocalNavController
import com.hamdan.forzenbook.view.NavigationDestinations
import com.hamdan.forzenbook.view.composables.ErrorText
import com.hamdan.forzenbook.view.composables.ForzenbookDialog
import com.hamdan.forzenbook.view.composables.InputField
import com.hamdan.forzenbook.view.composables.LoadingButton
import com.hamdan.forzenbook.view.composables.LoginBackgroundColumn
import com.hamdan.forzenbook.view.composables.LoginTitleSection
import com.hamdan.forzenbook.view.composables.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.view.composables.SubmitButton
import com.hamdan.forzenbook.viewmodels.LoginViewModel

@Composable
fun MainLoginContent(
    state: LoginViewModel.LoginState,
    onInfoDismiss: () -> Unit,
    onErrorDismiss: () -> Unit,
    onTextChange: (Entry, Entry, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    LoginBackgroundColumn {
        Image(
            modifier = Modifier.size(ForzenbookTheme.dimens.iconSizeLarge),
            painter = painterResource(id = R.drawable.logo_render_full_notext),
            contentDescription = stringResource(id = R.string.lion_icon),
        )
        LoginTitleSection(stringResource(id = R.string.app_name))
        Content(
            stateEmail = state.email,
            stateCode = state.code,
            isInputtingCode = state.inputtingCode,
            hasError = state.hasError,
            showInfo = state.showInfoDialog,
            isLoading = state.isLoading,
            onInfoDismiss = onInfoDismiss,
            onErrorDismiss = onErrorDismiss,
            onTextChange = onTextChange,
            onSubmission = onSubmission
        )
    }
    if (state.isLoading) {
        PreventScreenActionsDuringLoad()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Content(
    stateEmail: Entry,
    stateCode: Entry,
    isInputtingCode: Boolean,
    hasError: Boolean,
    showInfo: Boolean,
    isLoading: Boolean,
    onInfoDismiss: () -> Unit,
    onErrorDismiss: () -> Unit,
    onTextChange: (Entry, Entry, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    var email = stateEmail.text
    var emailError = stateEmail.error
    var code = stateCode.text
    var codeError = stateCode.error
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    InputField(
        label = stringResource(R.string.login_email_prompt),
        value = email,
        onValueChange = {
            email = it
            onTextChange(Entry(email, emailError), Entry(code, codeError), isInputtingCode)
        },
        imeAction = if (isInputtingCode) ImeAction.Next else ImeAction.Done,
        onNext = { focusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            keyboardController?.hide()
            if (emailError.isValid()) {
                onSubmission()
            }
        },
    )
    if (email.isNotEmpty() && !emailError.isValid()) {
        if (emailError == LoginError.EmailError.Length) {
            ErrorText(error = stringResource(R.string.login_email_error_length))
        } else if (emailError == LoginError.EmailError.InvalidFormat) {
            ErrorText(error = stringResource(R.string.login_email_error_format))
        }
    }
    if (isInputtingCode) {
        InputField(
            label = stringResource(R.string.login_code_prompt), value = code,
            onValueChange = {
                code = it
                onTextChange(Entry(email, emailError), Entry(code, codeError), isInputtingCode)
            },
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            onDone = {
                keyboardController?.hide()
                if (emailError.isValid() && codeError.isValid()) {
                    onSubmission()
                }
            },
        )
        if (code.isNotEmpty() && !codeError.isValid()) {
            if (codeError == LoginError.CodeError.Length) {
                ErrorText(error = stringResource(R.string.login_code_error))
            }
        }
    }
    Spacer(modifier = Modifier.height(ForzenbookTheme.dimens.mediumPad_1))
    if (isLoading) {
        LoadingButton()
    } else {
        SubmitButton(
            onSubmission = onSubmission,
            label = stringResource(R.string.login_button_text),
            enabled = if (!isInputtingCode) {
                emailError.isValid()
            } else {
                emailError.isValid() && codeError.isValid()
            }
        )
    }
    Spacer(modifier = Modifier.height(ForzenbookTheme.dimens.mediumPad_1))
    val navController = LocalNavController.current
    Text(
        text = stringResource(R.string.login_create_account_text),
        fontSize = ForzenbookTheme.typography.h2.fontSize,
        modifier = Modifier
            .clickable {
                navController?.navigate(NavigationDestinations.CREATE_ACCOUNT)
            }
            .padding(ForzenbookTheme.dimens.smallPad_2)
    )
    if (hasError) {
        ForzenbookDialog(
            title = stringResource(R.string.login_error_title),
            body = stringResource(R.string.login_error),
            buttonText = stringResource(id = R.string.login_confirm_error),
            onDismiss = onErrorDismiss
        )
    }
    if (showInfo) {
        ForzenbookDialog(
            title = stringResource(R.string.login_info_title),
            body = stringResource(R.string.login_info),
            buttonText = stringResource(id = R.string.login_confirm_info),
            onDismiss = onInfoDismiss
        )
    }
}
