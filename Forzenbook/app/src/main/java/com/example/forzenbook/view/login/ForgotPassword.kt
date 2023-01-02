package com.example.forzenbook.view.login

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forzenbook.R
import com.example.forzenbook.view.LocalNavController
import com.example.forzenbook.view.composeutils.*
import com.example.forzenbook.view.composeutils.ComposeUtils.COLOR_BLACK
import com.example.forzenbook.viewmodels.LoginViewModel

@Composable
fun ForgotPasswordContent(
    state: LoginViewModel.ForgotPasswordState,
    onTextChange: (String, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ComposeUtils.COLOR_LION_YELLOW),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            backgroundColor = ComposeUtils.COLOR_LION_YELLOW
        ) {
            val navController = LocalNavController.current
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_arrow_back_ios_24),
                    contentDescription = "back arrow",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController?.navigateUp()
                        },
                    tint = COLOR_BLACK
                )
            }
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Forgot Password",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = COLOR_BLACK
                )
            }
            Column(modifier = Modifier.weight(1f)) {
            }
        }

        when (state) {
            is LoginViewModel.ForgotPasswordState.Error -> {
                ErrorContent()
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
private fun ErrorContent() {
    ErrorWithIcon(
        errorText = "There was an error when\ntrying to send a request for reset",
        buttonText = "Ok"
    )
}

@Composable
private fun SuccessContent() {
    Text(
        text = "You can only make a reset request once every 30 seconds, please wait",
        modifier = Modifier.padding(horizontal = 40.dp, vertical = 20.dp)
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
        modifier = Modifier.padding(horizontal = 40.dp, vertical = 12.dp),
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
            modifier = Modifier.padding(8.dp)
        )
    }
    SubmitButton(
        onSubmission = onSubmission,
        label = "Submit",
        enabled = !emailError
    )
}
