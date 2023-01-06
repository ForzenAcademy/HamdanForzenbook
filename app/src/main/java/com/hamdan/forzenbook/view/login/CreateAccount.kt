package com.hamdan.forzenbook.view.login

import android.app.DatePickerDialog
import android.text.TextUtils
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.theme.ForzenbookTheme
import com.hamdan.forzenbook.theme.IconSizeValues
import com.hamdan.forzenbook.view.composeutils.DimBackgroundLoad
import com.hamdan.forzenbook.view.composeutils.ErrorWithIcon
import com.hamdan.forzenbook.view.composeutils.InputField
import com.hamdan.forzenbook.view.composeutils.LoginBackgroundColumn
import com.hamdan.forzenbook.view.composeutils.LoginTopBar
import com.hamdan.forzenbook.view.composeutils.SubmitButton
import com.hamdan.forzenbook.view.composeutils.validateEmail
import com.hamdan.forzenbook.view.login.LoginSharedConstants.EMAIL_LENGTH_LIMIT
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import java.util.Calendar

private const val AGE_MINIMUM = 13
private const val NAME_LENGTH_LIMIT = 20
private const val LOCATION_LENGTH_LIMIT = 64
private const val EXPECTED_ARRAY_SIZE = 3

@Composable
fun CreateAccountContent(
    state: LoginViewModel.CreateAccountState,
    onErrorSubmit: () -> Unit,
    onTextChange: (String, String, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    LoginBackgroundColumn {
        LoginTopBar(topText = stringResource(R.string.top_bar_text_create_account))
        when (state) {
            is LoginViewModel.CreateAccountState.Error -> ErrorContent(onErrorSubmit)
            is LoginViewModel.CreateAccountState.Loading -> {}
            is LoginViewModel.CreateAccountState.UserInputting -> {
                state.apply {
                    UserInputtingContent(
                        firstName,
                        lastName,
                        birthDay,
                        email,
                        location,
                        firstError,
                        lastError,
                        birthError,
                        emailError,
                        locationError,
                        onTextChange,
                        onSubmission
                    )
                }
            }
        }
    }
    if (state is LoginViewModel.CreateAccountState.Loading) {
        DimBackgroundLoad()
    }
}

@Composable
private fun ErrorContent(onErrorSubmit: () -> Unit) {
    ErrorWithIcon(
        errorText = stringResource(R.string.error_create_account),
        buttonText = stringResource(R.string.create_account_confirm_error),
        onSubmission = onErrorSubmit
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun UserInputtingContent(
    stateFirstName: String,
    stateLastName: String,
    stateBirthDate: String,
    stateEmail: String,
    stateLocation: String,
    stateFirstError: Boolean = true,
    stateLastError: Boolean = true,
    stateBirthError: Boolean = true,
    stateEmailError: Boolean = true,
    stateLocationError: Boolean = true,
    onTextChange: (String, String, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean) -> Unit,
    onSubmission: () -> Unit
) {
    var firstName = stateFirstName
    var firstNameError = stateFirstError
    var lastName = stateLastName
    var lastNameError = stateLastError
    var birthDate = stateBirthDate
    var birthError = stateBirthError
    var email = stateEmail
    var emailError = stateEmailError
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
                birthDate = "$Year-$Month-$DayOfMonth"
                val split = birthDate.split("-")
                if (split.size == EXPECTED_ARRAY_SIZE) {
                    val year = split[0].toIntOrNull()
                    val month = split[1].toIntOrNull()
                    val day = split[2].toIntOrNull()
                    year?.let {
                        month?.let {
                            day?.let {
                                birthError = (
                                    (currentYear - year < AGE_MINIMUM) ||
                                        ((currentYear - year == AGE_MINIMUM && currentMonth - month < 0)) ||
                                        ((currentYear - year == AGE_MINIMUM && currentMonth - month == 0 && currentDay - day < 0))
                                    )
                                onTextChange(
                                    firstName,
                                    lastName,
                                    birthDate,
                                    email,
                                    location,
                                    firstNameError,
                                    lastNameError,
                                    birthError,
                                    emailError,
                                    locationError
                                )
                            }
                        }
                    }
                }
            },
            selectedYear,
            selectedMonth,
            selectedDay
        )
    InputField(
        label = stringResource(R.string.create_account_first_name_prompt),
        value = firstName,
        onValueChange = {
            firstName = it
            firstNameError = firstName.length > NAME_LENGTH_LIMIT
            onTextChange(
                firstName, lastName, birthDate, email, location,
                firstNameError, lastNameError, birthError, emailError, locationError
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (firstNameError && firstName != "") {
        Text(text = stringResource(R.string.create_account_first_name_error))
    }
    InputField(
        label = stringResource(R.string.create_account_last_name_prompt),
        value = lastName,
        onValueChange = {
            lastName = it
            lastNameError = lastName.length > NAME_LENGTH_LIMIT
            onTextChange(
                firstName, lastName, birthDate, email, location,
                firstNameError, lastNameError, birthError, emailError, locationError
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (lastNameError && lastName != "") {
        Text(text = stringResource(R.string.create_account_last_name_error))
    }
    TextField(
        value = if (birthDate == "") stringResource(R.string.create_account_birth_date_prompt) else birthDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = com.hamdan.forzenbook.theme.PaddingValues.largePad_4,
                vertical = com.hamdan.forzenbook.theme.PaddingValues.smallPad_2
            )
            .clickable {
                datePicker.show()
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier
                    .size(IconSizeValues.small_1)
            )
        },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            disabledTextColor = ForzenbookTheme.colors.colors.onBackground,
            disabledPlaceholderColor = ForzenbookTheme.colors.colors.onBackground,
            disabledLabelColor = ForzenbookTheme.colors.colors.onBackground,
            // For Icons
            disabledLeadingIconColor = MaterialTheme.colors.primary,
            disabledTrailingIconColor = MaterialTheme.colors.secondary
        ),
        singleLine = true
    )
    if (birthError && birthDate != "") {
        Text(text = stringResource(R.string.create_account_birth_date_error))
    }
    InputField(
        label = stringResource(R.string.create_account_email_prompt),
        value = email,
        onValueChange = {
            email = it
            emailError =
                email.length > EMAIL_LENGTH_LIMIT || !TextUtils.isEmpty(email) &&
                !validateEmail(email)
            onTextChange(
                firstName, lastName, birthDate, email, location,
                firstNameError, lastNameError, birthError, emailError, locationError
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (emailError && email != "") {
        Text(text = stringResource(R.string.create_account_email_error))
    }
    InputField(
        label = stringResource(R.string.create_account_location_prompt),
        value = location,
        onValueChange = {
            location = it
            locationError = location.length > LOCATION_LENGTH_LIMIT
            onTextChange(
                firstName, lastName, birthDate, email, location,
                firstNameError, lastNameError, birthError, emailError, locationError
            )
        },
        imeAction = ImeAction.Done,
        onDone = {
            keyboardController?.hide()
            if (!(emailError || birthError || locationError || lastNameError || firstNameError)) {
                onSubmission()
            }
        }
    )
    if (locationError && location != "") {
        Text(text = stringResource(R.string.create_account_location_error))
    }
    Spacer(modifier = Modifier.height(com.hamdan.forzenbook.theme.PaddingValues.smallPad_3))
    SubmitButton(
        onSubmission = onSubmission,
        label = stringResource(R.string.create_account_submit),
        enabled = !(emailError || birthError || locationError || firstNameError || lastNameError)
    )
    Spacer(modifier = Modifier.height(60.dp))
}
