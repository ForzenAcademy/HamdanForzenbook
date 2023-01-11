package com.hamdan.forzenbook.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
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
import com.hamdan.forzenbook.theme.IconSizeValues
import com.hamdan.forzenbook.theme.PaddingValues
import com.hamdan.forzenbook.view.LocalNavController
import com.hamdan.forzenbook.view.NavigationDestinations
import com.hamdan.forzenbook.view.composeutils.ComposeUtils.APP_NAME
import com.hamdan.forzenbook.view.composeutils.DimBackgroundLoad
import com.hamdan.forzenbook.view.composeutils.InputField
import com.hamdan.forzenbook.view.composeutils.LoginBackgroundColumn
import com.hamdan.forzenbook.view.composeutils.LoginTitleSection
import com.hamdan.forzenbook.view.composeutils.SubmitButton
import com.hamdan.forzenbook.view.composeutils.validateEmail
import com.hamdan.forzenbook.view.login.LoginSharedConstants.EMAIL_LENGTH_LIMIT
import com.hamdan.forzenbook.viewmodels.Entry
import com.hamdan.forzenbook.viewmodels.LoginViewModel

private const val CODE_LENGTH_MAX = 6

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
            modifier = Modifier.size(IconSizeValues.giga_1),
            painter = painterResource(id = R.drawable.logo_render_full_notext),
            contentDescription = stringResource(id = R.string.lion_icon),
        )
        LoginTitleSection(APP_NAME)
        Content(
            stateEmail = state.email,
            stateCode = state.code,
            isInputtingCode = state.inputtingCode,
            hasError = state.hasError,
            showInfo = state.showInfoDialog,
            onInfoDismiss = onInfoDismiss,
            onErrorDismiss = onErrorDismiss,
            onTextChange = onTextChange,
            onSubmission = onSubmission
        )
    }
    if (state.isLoading) {
        DimBackgroundLoad()
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

    if (isInputtingCode) {
        InputField(
            label = stringResource(R.string.login_email_prompt), value = email,
            onValueChange = {
                email = it
                emailError = email.length > EMAIL_LENGTH_LIMIT || !validateEmail(email)
                onTextChange(Entry(email, emailError), Entry(code, codeError), isInputtingCode)
            },
            imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )
        if (emailError && email.isNotEmpty()) {
            Text(text = stringResource(R.string.login_email_error))
        }
        InputField(
            label = stringResource(R.string.login_code_prompt), value = code,
            onValueChange = {
                code = it
                codeError =
                    (code.length > CODE_LENGTH_MAX)
                onTextChange(Entry(email, emailError), Entry(code, codeError), isInputtingCode)
            },
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
            onDone = {
                keyboardController?.hide()
                if (!emailError || !codeError) {
                    onSubmission()
                }
            },
        )
        if (codeError && code.isNotEmpty()) {
            Text(text = stringResource(R.string.login_code_error))
        }
    } else {
        InputField(
            label = stringResource(R.string.login_email_prompt), value = email,
            onValueChange = {
                email = it
                emailError = email.length > EMAIL_LENGTH_LIMIT || !validateEmail(email)
                onTextChange(Entry(email, emailError), Entry(code, codeError), isInputtingCode)
            },
            imeAction = ImeAction.Done,
            onDone = {
                keyboardController?.hide()
                if (!emailError) {
                    onSubmission()
                }
            },
        )
        if (emailError && email.isNotEmpty()) {
            Text(text = stringResource(R.string.login_email_error))
        }
    }
    Spacer(modifier = Modifier.height(PaddingValues.medPad_2))
    SubmitButton(
        onSubmission = onSubmission,
        label = stringResource(R.string.login_button_text),
        enabled = !(emailError)
    )
    Spacer(modifier = Modifier.height(PaddingValues.medPad_2))
    val navController = LocalNavController.current
    Text(
        text = stringResource(R.string.login_create_account_text),
        modifier = Modifier.clickable {
            navController?.navigate(NavigationDestinations.CREATE_ACCOUNT)
        }
    )
    if (hasError) {
        AlertDialog(
            onDismissRequest = { onErrorDismiss() },
            title = {
                Text(text = stringResource(R.string.login_error_title))
            },
            text = {
                Text(text = stringResource(R.string.login_error))
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.login_confirm_error),
                    modifier = Modifier
                        .padding(
                            end = PaddingValues.smallPad_2,
                            bottom = PaddingValues.smallPad_2
                        )
                        .clickable { onErrorDismiss() },
                )
            },
            modifier = Modifier
                .padding(PaddingValues.largePad_1),
        )
    }
    if (showInfo) {
        AlertDialog(
            onDismissRequest = { onInfoDismiss() },
            title = {
                Text(text = stringResource(R.string.login_info_title))
            },
            text = {
                Text(text = stringResource(R.string.login_info))
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.login_confirm_info),
                    modifier = Modifier
                        .padding(
                            end = PaddingValues.smallPad_2,
                            bottom = PaddingValues.smallPad_2
                        )
                        .clickable { onInfoDismiss() },
                )
            },
            modifier = Modifier
                .padding(PaddingValues.largePad_1),
        )
    }
}
