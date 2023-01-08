package com.hamdan.forzenbook.view.login

import android.text.TextUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.theme.IconSizeValues
import com.hamdan.forzenbook.theme.PaddingValues
import com.hamdan.forzenbook.view.LocalNavController
import com.hamdan.forzenbook.view.NavigationDestinations
import com.hamdan.forzenbook.view.composeutils.ComposeUtils.APP_NAME
import com.hamdan.forzenbook.view.composeutils.DimBackgroundLoad
import com.hamdan.forzenbook.view.composeutils.ErrorWithIcon
import com.hamdan.forzenbook.view.composeutils.InputField
import com.hamdan.forzenbook.view.composeutils.LoginBackgroundColumn
import com.hamdan.forzenbook.view.composeutils.LoginTitleSection
import com.hamdan.forzenbook.view.composeutils.SubmitButton
import com.hamdan.forzenbook.view.composeutils.validateEmail
import com.hamdan.forzenbook.viewmodels.LoginViewModel

@Composable
fun MainLoginContent(
    state: LoginViewModel.LoginState,
    onErrorSubmit: () -> Unit,
    onTextChange: (String, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    LoginBackgroundColumn {
        Image(
            modifier = Modifier.size(IconSizeValues.giga_1),
            painter = painterResource(id = R.drawable.logo_render_full_notext),
            contentDescription = "Yellow lion"
        )
        LoginTitleSection(APP_NAME)
        when (state) {
            is LoginViewModel.LoginState.Error -> {
                ErrorContent(onErrorSubmit)
            }
            is LoginViewModel.LoginState.Loading -> {
                state.apply {
                    UserInputtingContent(
                        email,
                        emailError,
                        onTextChange,
                        onSubmission
                    )
                }
            }
            is LoginViewModel.LoginState.UserInputting -> {
                state.apply {
                    UserInputtingContent(
                        email,
                        emailError,
                        onTextChange,
                        onSubmission
                    )
                }
            }
        }
    }
    if (state is LoginViewModel.LoginState.Loading) {
        DimBackgroundLoad()
    }
}

@Composable
private fun ErrorContent(onErrorSubmit: () -> Unit) {
    ErrorWithIcon(
        errorText = stringResource(R.string.login_error),
        buttonText = stringResource(R.string.login_confirm_error),
        onSubmission = onErrorSubmit
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserInputtingContent(
    stateEmail: String,
    stateEmailError: Boolean,
    onTextChange: (String, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    var email: String = stateEmail
    var emailError = stateEmailError
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    InputField(
        label = stringResource(R.string.login_email_prompt), value = email,
        onValueChange = {
            email = it
            emailError =
                ((email.length > 30) || (!TextUtils.isEmpty(email) && !validateEmail(email)))
            onTextChange(email, emailError)
        },
        imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (emailError && email != "") {
        Text(text = stringResource(R.string.login_email_error))
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
}
