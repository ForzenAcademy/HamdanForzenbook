package com.hamdan.forzenbook.view.login

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.theme.ForzenbookTheme
import com.hamdan.forzenbook.theme.IconSizeValues
import com.hamdan.forzenbook.view.composables.ErrorText
import com.hamdan.forzenbook.view.composables.ForzenbookDialog
import com.hamdan.forzenbook.view.composables.InputField
import com.hamdan.forzenbook.view.composables.LoadingButton
import com.hamdan.forzenbook.view.composables.LoginBackgroundColumn
import com.hamdan.forzenbook.view.composables.LoginTopBar
import com.hamdan.forzenbook.view.composables.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.view.composables.SubmitButton
import com.hamdan.forzenbook.view.composables.validateEmail
import com.hamdan.forzenbook.view.login.LoginSharedConstants.EMAIL_LENGTH_LIMIT
import com.hamdan.forzenbook.viewmodels.Entry
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar

private const val AGE_MINIMUM = 13
private const val NAME_LENGTH_LIMIT = 20
private const val LOCATION_LENGTH_LIMIT = 64
private const val ONE_LINE = 1

@Composable
fun CreateAccountContent(
    state: LoginViewModel.CreateAccountState,
    onErrorDismiss: () -> Unit,
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit,
    onDateFieldClick: () -> Unit,
    onDateSubmission: () -> Unit,
    onDateDismiss: () -> Unit,
    onSubmission: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { LoginTopBar(topText = stringResource(R.string.top_bar_text_create_account)) },
    ) { padding ->
        LoginBackgroundColumn(modifier = Modifier.padding(padding)) {
            Content(
                stateError = state.errorId,
                stateFirstName = state.firstName,
                stateLastName = state.lastName,
                stateBirthDate = state.birthDay,
                stateEmail = state.email,
                stateLocation = state.location,
                isDialogOpen = state.isDateDialogOpen,
                isLoading = state.isLoading,
                onTextChange = onTextChange,
                onDateSubmission = onDateSubmission,
                onDateDismiss = onDateDismiss,
                onSubmission = onSubmission,
                onErrorDismiss = onErrorDismiss,
                onDateFieldClick = onDateFieldClick,
            )
        }
        if (state.isLoading) {
            PreventScreenActionsDuringLoad()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Content(
    stateError: Int?,
    stateFirstName: Entry,
    stateLastName: Entry,
    stateBirthDate: Entry,
    stateEmail: Entry,
    stateLocation: Entry,
    isDialogOpen: Boolean,
    isLoading: Boolean,
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit,
    onDateSubmission: () -> Unit,
    onDateDismiss: () -> Unit,
    onSubmission: () -> Unit,
    onErrorDismiss: () -> Unit,
    onDateFieldClick: () -> Unit,
) {
    var firstName = stateFirstName.text
    var firstNameError = stateFirstName.error
    var lastName = stateLastName.text
    var lastNameError = stateLastName.error
    var birthDate = stateBirthDate.text
    var birthError = stateBirthDate.error
    var email = stateEmail.text
    var emailError = stateEmail.error
    var location = stateLocation.text
    var locationError = stateLocation.error

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    InputField(
        label = stringResource(R.string.create_account_first_name_prompt),
        value = firstName,
        onValueChange = {
            firstName = it
            firstNameError = if (firstName.length > NAME_LENGTH_LIMIT) {
                LoginError.NameError.Length
            } else if (!firstName.all { name -> name.isLetter() }) {
                LoginError.NameError.InvalidCharacters
            } else LoginError.NameError.Valid
            onTextChange(
                Entry(firstName, firstNameError),
                Entry(lastName, lastNameError),
                Entry(birthDate, birthError),
                Entry(email, emailError),
                Entry(location, locationError),
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (firstName.isNotEmpty() && !firstNameError.isValid()) {
        if (firstNameError == LoginError.NameError.Length) {
            ErrorText(error = stringResource(R.string.create_account_first_name_error_length))
        } else if (firstNameError == LoginError.NameError.InvalidCharacters) {
            ErrorText(error = stringResource(R.string.create_account_first_name_error_invalid_characters))
        }
    }
    InputField(
        label = stringResource(R.string.create_account_last_name_prompt),
        value = lastName,
        onValueChange = {
            lastName = it
            lastNameError = if (lastName.length > NAME_LENGTH_LIMIT) {
                LoginError.NameError.Length
            } else if (!lastName.all { name -> name.isLetter() }) {
                LoginError.NameError.InvalidCharacters
            } else LoginError.NameError.Valid
            onTextChange(
                Entry(firstName, firstNameError),
                Entry(lastName, lastNameError),
                Entry(birthDate, birthError),
                Entry(email, emailError),
                Entry(location, locationError),
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (lastName.isNotEmpty() && !lastNameError.isValid()) {
        if (lastNameError == LoginError.NameError.Length) {
            ErrorText(error = stringResource(R.string.create_account_last_name_error_length))
        } else if (lastNameError == LoginError.NameError.InvalidCharacters) {
            ErrorText(error = stringResource(R.string.create_account_last_name_error_invalid_characters))
        }
    }
    TextField(
        value = birthDate,
        label = { Text(text = stringResource(R.string.create_account_birth_date_prompt)) },
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = ForzenbookTheme.dimens.largePad_2,
                vertical = ForzenbookTheme.dimens.smallPad_1
            )
            .clickable {
                onDateFieldClick()
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.calendar_icon),
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
        textStyle = TextStyle(fontSize = ForzenbookTheme.typography.h2.fontSize),
        maxLines = ONE_LINE,
    )
    if (birthDate.isNotEmpty() && !birthError.isValid()) {
        ErrorText(error = stringResource(R.string.create_account_birth_date_error))
    }
    InputField(
        label = stringResource(R.string.create_account_email_prompt),
        value = email,
        onValueChange = {
            email = it
            emailError = if (email.length > EMAIL_LENGTH_LIMIT) {
                LoginError.EmailError.Length
            } else if (!validateEmail(email)) {
                LoginError.EmailError.InvalidFormat
            } else LoginError.EmailError.Valid
            onTextChange(
                Entry(firstName, firstNameError),
                Entry(lastName, lastNameError),
                Entry(birthDate, birthError),
                Entry(email, emailError),
                Entry(location, locationError),
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (email.isNotEmpty() && !emailError.isValid()) {
        if (emailError == LoginError.EmailError.Length) {
            ErrorText(error = stringResource(R.string.login_email_error_length))
        } else if (emailError == LoginError.EmailError.InvalidFormat) {
            ErrorText(error = stringResource(R.string.login_email_error_format))
        }
    }
    InputField(
        label = stringResource(R.string.create_account_location_prompt),
        value = location,
        onValueChange = {
            location = it
            locationError = if (location.length > LOCATION_LENGTH_LIMIT) {
                LoginError.LocationError.Length
            } else LoginError.LocationError.Valid
            onTextChange(
                Entry(firstName, firstNameError),
                Entry(lastName, lastNameError),
                Entry(birthDate, birthError),
                Entry(email, emailError),
                Entry(location, locationError),
            )
        },
        imeAction = ImeAction.Done,
        onDone = {
            keyboardController?.hide()
            if (emailError.isValid() && birthError.isValid() && locationError.isValid() && lastNameError.isValid() && firstNameError.isValid()) {
                onSubmission()
            }
        }
    )
    if (location.isNotEmpty() && !locationError.isValid()) {
        if (locationError == LoginError.LocationError.Length) {
            ErrorText(error = stringResource(R.string.create_account_location_error))
        }
    }
    Spacer(modifier = Modifier.height(ForzenbookTheme.dimens.smallPad_2))
    if (isLoading) {
        LoadingButton()
    } else {
        SubmitButton(
            onSubmission = onSubmission,
            label = stringResource(R.string.create_account_submit),
            enabled = (emailError.isValid() && birthError.isValid() && locationError.isValid() && lastNameError.isValid() && firstNameError.isValid())
        )
    }
    Spacer(modifier = Modifier.height(60.dp))
    if (stateError != null) {
        ForzenbookDialog(
            title = stringResource(R.string.create_account_error_title),
            body = stringResource(stateError),
            buttonText = stringResource(id = R.string.create_account_confirm_error),
            onDismiss = onErrorDismiss
        )
    }
    val context = LocalContext.current
    if (isDialogOpen) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val minDate = LocalDateTime.of(currentYear - AGE_MINIMUM, currentMonth, currentDay, 0, 0)
        var selectedYear: Int = currentYear
        var selectedMonth: Int = currentMonth
        var selectedDay: Int = currentDay
        if (birthDate.isNotEmpty()) {
            val split = birthDate.split("-")
            selectedYear = split[0].toInt()
            selectedMonth = split[1].toInt()
            selectedDay = split[2].toInt()
        }
        // TODO change how the date format is FA-84
        LaunchedEffect(Unit) {
            DatePickerDialog(
                context,
                R.style.MySpinnerDatePickerStyle,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    val actualMonth = month + 1
                    birthDate = if (actualMonth < 10) {
                        context.getString(R.string.create_account_date, year, actualMonth, day)
                    } else context.getString(
                        R.string.create_account_date_tens,
                        year,
                        actualMonth,
                        day
                    )
                    val selectedDate = LocalDate.parse(birthDate)
                    birthError = if (selectedDate.isAfter(minDate.toLocalDate())) {
                        LoginError.BirthDateError.TooYoung
                    } else LoginError.BirthDateError.Valid
                    onTextChange(
                        Entry(firstName, firstNameError),
                        Entry(lastName, lastNameError),
                        Entry(birthDate, birthError),
                        Entry(email, emailError),
                        Entry(location, locationError),
                    )
                    onDateSubmission()
                },
                selectedYear,
                selectedMonth,
                selectedDay
            ).apply {
                setOnDismissListener { onDateDismiss() }
            }.show()
        }
    }
}
