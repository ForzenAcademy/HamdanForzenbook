package com.example.forzenbook.view.login

import android.app.DatePickerDialog
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forzenbook.R
import com.example.forzenbook.view.LocalNavController
import com.example.forzenbook.view.composeutils.*
import com.example.forzenbook.view.composeutils.ComposeUtils.COLOR_BLACK
import com.example.forzenbook.view.composeutils.ComposeUtils.COLOR_LION_YELLOW
import com.example.forzenbook.view.composeutils.ComposeUtils.tempPadding
import com.example.forzenbook.viewmodels.LoginViewModel
import java.util.*

@Composable
fun CreateAccountContent(
    state: LoginViewModel.CreateAccountState,
    onTextChange: (String, String, String, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
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
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            backgroundColor = COLOR_LION_YELLOW
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create Account", fontSize = 20.sp, textAlign = TextAlign.Center,
                    color = COLOR_BLACK
                )
            }
            Column(modifier = Modifier.weight(1f)) {
            }
        }

        when (state) {
            is LoginViewModel.CreateAccountState.Error -> ErrorContent()
            is LoginViewModel.CreateAccountState.Loading -> {}
            is LoginViewModel.CreateAccountState.UserInputting -> UserInputtingContent(
                state.firstName,
                state.lastName,
                state.birthDay,
                state.email,
                state.password,
                state.location,
                state.firstError,
                state.lastError,
                state.birthError,
                state.emailError,
                state.passwordError,
                state.locationError,
                onTextChange,
                onSubmission
            )
        }
    }
    if (state is LoginViewModel.CreateAccountState.Loading) {
        DimBackgroundLoad()
    }
}


@Composable
private fun ErrorContent() {
    ErrorWithIcon(
        errorText = "There was an error when\ntrying to send a request for account creation",
        buttonText = "Ok"
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserInputtingContent(
    stateFirstName: String,
    stateLastName: String,
    stateBirthDate: String,
    stateEmail: String,
    statePassword: String,
    stateLocation: String,
    stateFirstError: Boolean = true,
    stateLastError: Boolean = true,
    stateBirthError: Boolean = true,
    stateEmailError: Boolean = true,
    statePasswordError: Boolean = true,
    stateLocationError: Boolean = true,
    onTextChange: (String, String, String, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onSubmission: () -> Unit
) {

    //ASK NIC IS THERE A BETTER WAY TO DO THIS? Other than using a lambda for a new set of booleans in the state object
    var firstName = stateFirstName
    var firstNameError = stateFirstError
    var lastName = stateLastName
    var lastNameError = stateLastError
    var birthDate = stateBirthDate
    var birthError = stateBirthError
    var email = stateEmail
    var emailError = stateEmailError
    var password = statePassword
    var passwordError = statePasswordError
    var location = stateLocation
    var locationError = stateLocationError

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedYear: Int = currentYear
    var selectedMonth: Int = currentMonth
    var selectedDay: Int = currentDay
    if (birthDate != "") {
        val split = birthDate.split("-")
        selectedYear = split[0].toInt()
        selectedMonth = split[1].toInt()
        selectedDay = split[2].toInt()
    }

    val datePicker =
        DatePickerDialog(
            context,
            { _: DatePicker, Year: Int, Month: Int, DayOfMonth: Int ->
                birthDate = "$Year-${Month}-$DayOfMonth"
                val split = birthDate.split("-")
                val year = split[0].toInt()
                val month = split[1].toInt()
                val day = split[2].toInt()
                birthError = ((currentYear - year < 13) ||
                        ((currentYear - year == 13 && currentMonth - month < 0)) ||
                        ((currentYear - year == 13 && currentMonth - month == 0 && currentDay - day < 0)))
                onTextChange(
                    firstName,
                    lastName,
                    birthDate,
                    email,
                    password,
                    location,
                    firstNameError,
                    lastNameError,
                    birthError,
                    emailError,
                    passwordError,
                    locationError
                )
            }, selectedYear,
            selectedMonth,
            selectedDay
        )
    InputField(label = "First Name", value = firstName, onValueChange = {
        firstName = it
        firstNameError = firstName.length > 20
        onTextChange(
            firstName, lastName, birthDate, email, password, location,
            firstNameError, lastNameError, birthError, emailError, passwordError, locationError
        )
    }, imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) })
    if (firstNameError && firstName != "") {
        Text(text = "First Names can only be 20 characters or less")
    }
    InputField(label = "Last Name", value = lastName, onValueChange = {
        lastName = it
        lastNameError = lastName.length > 20
        onTextChange(
            firstName, lastName, birthDate, email, password, location,
            firstNameError, lastNameError, birthError, emailError, passwordError, locationError
        )
    }, imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) })
    if (lastNameError && lastName != "") {
        Text(text = "Last Names can only be 20 characters or less")
    }
    TextField(
        value = if (birthDate == "") "Birth Date" else birthDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = tempPadding)
            .clickable {
                datePicker.show()
                Log.v("Hamdan", birthDate)
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
        },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            disabledTextColor = MaterialTheme.colors.onSurface,
            disabledPlaceholderColor = MaterialTheme.colors.primary,
            disabledLabelColor = MaterialTheme.colors.secondary,
            //For Icons
            disabledLeadingIconColor = MaterialTheme.colors.primary,
            disabledTrailingIconColor = MaterialTheme.colors.secondary
        ),
        singleLine = true
    )
    if (birthError && birthDate != "") {
        Text(text = "Invalid birth date, must be 13 or older")
    }
    InputField(label = "Email", value = email, onValueChange = {
        email = it
        emailError =
            ((email.length > 30) || (!TextUtils.isEmpty(email) && !validateEmail(email)))
        onTextChange(
            firstName, lastName, birthDate, email, password, location,
            firstNameError, lastNameError, birthError, emailError, passwordError, locationError
        )
    }, imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) })
    if (emailError && email != "") {
        Text(text = "Invalid email or email length longer than 30 characters")
    }
    InputField(label = "Password", value = password, onValueChange = {
        password = it
        passwordError = password.length > 30
        onTextChange(
            firstName, lastName, birthDate, email, password, location,
            firstNameError, lastNameError, birthError, emailError, passwordError, locationError
        )

    }, imeAction = ImeAction.Next, onNext = { focusManager.moveFocus(FocusDirection.Down) })
    if (passwordError && password != "") {
        Text(text = "Passwords cannot be longer than 30 characters")
    }
    InputField(label = "Location", value = location, onValueChange = {
        location = it
        locationError = location.length > 64
        onTextChange(
            firstName, lastName, birthDate, email, password, location,
            firstNameError, lastNameError, birthError, emailError, passwordError, locationError
        )
    }, imeAction = ImeAction.Done, onDone = {

        keyboardController?.hide()
        if (!(emailError || birthError || locationError || lastNameError || firstNameError || passwordError)) {
            onSubmission()
        }
    })
    if (locationError && location != "") {
        Text(text = "Locations cannot be longer than 64 characters")
    }
    Spacer(modifier = Modifier.height(12.dp))
    SubmitButton(
        onSubmission = onSubmission,
        label = "Create Account",
        enabled = !(passwordError || emailError || birthError || locationError || firstNameError || lastNameError)
    )
    Spacer(modifier = Modifier.height(60.dp))

}
