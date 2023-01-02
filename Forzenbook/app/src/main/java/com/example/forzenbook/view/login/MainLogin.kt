package com.example.forzenbook.view.login

import android.text.TextUtils
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.forzenbook.R
import com.example.forzenbook.view.LocalNavController
import com.example.forzenbook.view.NavigationDestinations
import com.example.forzenbook.view.composeutils.*
import com.example.forzenbook.view.composeutils.ComposeUtils.APP_NAME
import com.example.forzenbook.view.composeutils.ComposeUtils.COLOR_LION_YELLOW
import com.example.forzenbook.viewmodels.LoginViewModel

@Composable
fun MainLoginContent(
    state: LoginViewModel.LoginState,
    onTextChange: (String, String, Boolean, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(COLOR_LION_YELLOW)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.logo_render_full_notext),
            contentDescription = "Yellow lion"
        )
        LoginTitleSection(APP_NAME)
        when (state) {
            is LoginViewModel.LoginState.Error -> {
                ErrorContent()
            }
            is LoginViewModel.LoginState.Loading -> {
                UserInputtingContent(
                    state.email,
                    state.password,
                    state.emailError,
                    state.passwordError,
                    onTextChange,
                    onSubmission
                )
            }
            is LoginViewModel.LoginState.UserInputting -> {
                UserInputtingContent(
                    state.email,
                    state.password,
                    state.emailError,
                    state.passwordError,
                    onTextChange,
                    onSubmission
                )
            }
        }
    }
    if (state is LoginViewModel.LoginState.Loading) {
        DimBackgroundLoad()
    }
}

@Composable
private fun ErrorContent() {
    ErrorWithIcon(
        errorText = "There was an error trying to login",
        buttonText = "Ok"
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserInputtingContent(
    stateEmail: String,
    statePassword: String,
    stateEmailError: Boolean,
    statePasswordError: Boolean,
    onTextChange: (String, String, Boolean, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    var email: String = stateEmail
    var password: String = statePassword
    var emailError = stateEmailError
    var passwordError = statePasswordError
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    InputField(
        label = "Email", value = email,
        onValueChange = {
            email = it
            emailError =
                ((email.length > 30) || (!TextUtils.isEmpty(email) && !validateEmail(email)))
            onTextChange(email, password, emailError, passwordError)
        },
        imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (emailError && email != "") {
        Text(text = "Invalid email or email length longer than 30 characters")
    }
    InputField(
        label = "Password", value = password,
        onValueChange = {

            password = it
            passwordError = password.length > 30
            onTextChange(email, password, emailError, passwordError)
        },
        visualTransformation = PasswordVisualTransformation(), imeAction = ImeAction.Done,
        onDone = {
            keyboardController?.hide()
            if (!(emailError || passwordError)) {
                onSubmission()
            }
        },
    )
    if (passwordError && password != "") {
        Text(text = "Passwords cannot be longer than 30 characters")
    }
    Spacer(modifier = Modifier.height(20.dp))
    SubmitButton(
        onSubmission = onSubmission,
        label = "Login",
        enabled = !(emailError || passwordError)
    )
    Spacer(modifier = Modifier.height(20.dp))
    val navController = LocalNavController.current
    Text(text = "Forgot Password", modifier = Modifier.clickable {
        navController?.navigate(NavigationDestinations.FORGOT_PASSWORD)
    })
    Text(text = "Create Account", modifier = Modifier.clickable {
        navController?.navigate(NavigationDestinations.CREATE_ACCOUNT)
    })
}





