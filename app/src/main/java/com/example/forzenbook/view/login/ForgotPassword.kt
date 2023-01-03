package com.example.forzenbook.view.login

import android.text.TextUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forzenbook.theme.PaddingValues
import com.example.forzenbook.view.composeutils.*
import com.example.forzenbook.viewmodels.LoginViewModel

@Composable
fun ForgotPasswordContent(
    state: LoginViewModel.ForgotPasswordState,
    onErrorSubmit: () -> Unit,
    onTextChange: (String, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    LoginBackgroundColumn {
        LoginTopBar(topText = "Forgot Password")

        when (state) {
            is LoginViewModel.ForgotPasswordState.Error -> {
                ErrorContent(onErrorSubmit)
            }
            is LoginViewModel.ForgotPasswordState.Loading -> {
                UserInputtingContent(state.email, state.emailError, onTextChange, onSubmission)
            }
            is LoginViewModel.ForgotPasswordState.Success -> {
                SuccessContent()
            }
            is LoginViewModel.ForgotPasswordState.UserInputting -> {
                UserInputtingContent(state.email, state.emailError, onTextChange, onSubmission)
            }
        }
    }
    if (state is LoginViewModel.ForgotPasswordState.Loading) {
        DimBackgroundLoad()
    }
}

@Composable
private fun ErrorContent(onErrorSubmit: () -> Unit) {
    ErrorWithIcon(
        errorText = "There was an error when\ntrying to send a request for reset",
        buttonText = "Ok",
        onSubmission = onErrorSubmit
    )
}

@Composable
private fun SuccessContent() {
    Text(
        text = "You can only make a reset request once every 30 seconds, please wait",
        modifier = Modifier.padding(horizontal = PaddingValues.largePad_4, vertical = PaddingValues.medPad_2)
    )
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }

}

@Composable
private fun UserInputtingContent(
    stateEmail: String,
    stateEmailError: Boolean = false,
    onTextChange: (String, Boolean) -> Unit = { _, _ -> },
    onSubmission: () -> Unit = {}
) {
    val pattern = Regex("[a-zA-z\\s]*")
    Text(
        text = "Please enter your email below, " +
                "when you submit you will receive an" +
                " email to reset your password",
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = PaddingValues.largePad_4, vertical = PaddingValues.smallPad_3),
        textAlign = TextAlign.Center
    )
    var email = stateEmail
    var emailError = stateEmailError
    InputField(label = "Email", value = email, onValueChange = {
        if (it.matches(pattern)) {
            email = it
        }
        emailError =
            ((email.length > 30) || (!TextUtils.isEmpty(email) && !validateEmail(email)))
        onTextChange(it, emailError)
    })
    if (emailError && email != "") {
        Text(
            text = "Invalid email or email length longer than 30 characters",
            modifier = Modifier.padding(PaddingValues.smallPad_2)
        )
    }
    SubmitButton(
        onSubmission = onSubmission,
        label = "Submit",
        enabled = !emailError
    )
}
