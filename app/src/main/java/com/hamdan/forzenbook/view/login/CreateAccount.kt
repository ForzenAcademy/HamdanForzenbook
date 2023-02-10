package com.hamdan.forzenbook.view.login

import android.app.DatePickerDialog
import android.content.Context
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
import com.hamdan.forzenbook.compose.core.composables.ErrorText
import com.hamdan.forzenbook.compose.core.composables.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composables.InputField
import com.hamdan.forzenbook.compose.core.composables.LoadingButton
import com.hamdan.forzenbook.compose.core.composables.LoginBackgroundColumn
import com.hamdan.forzenbook.compose.core.composables.LoginTopBar
import com.hamdan.forzenbook.compose.core.composables.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.compose.core.composables.SubmitButton
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import java.util.Calendar

private const val ONE_LINE = 1

@Composable
fun CreateAccountContent(
    state: LoginViewModel.CreateAccountState,
    onErrorDismiss: () -> Unit,
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit,
    onDateFieldClick: () -> Unit,
    onDateSubmission: () -> Unit,
    onDateDismiss: () -> Unit,
    onSubmission: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { LoginTopBar(topText = stringResource(R.string.top_bar_text_create_account), onNavigateUp = onNavigateUp) },
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
    var lastName = stateLastName.text
    var birthDate = stateBirthDate.text
    var email = stateEmail.text
    var location = stateLocation.text

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    InputField(
        label = stringResource(R.string.create_account_first_name_prompt),
        value = firstName,
        onValueChange = {
            firstName = it
            onTextChange(
                Entry(firstName, stateFirstName.error),
                Entry(lastName, stateLastName.error),
                Entry(birthDate, stateBirthDate.error),
                Entry(email, stateEmail.error),
                Entry(location, stateLocation.error),
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (firstName.isNotEmpty() && !stateFirstName.error.isValid()) {
        if (stateFirstName.error == LoginError.NameError.Length) {
            ErrorText(error = stringResource(R.string.create_account_first_name_error_length))
        } else if (stateFirstName.error == LoginError.NameError.InvalidCharacters) {
            ErrorText(error = stringResource(R.string.create_account_first_name_error_invalid_characters))
        }
    }
    InputField(
        label = stringResource(R.string.create_account_last_name_prompt),
        value = lastName,
        onValueChange = {
            lastName = it
            onTextChange(
                Entry(firstName, stateFirstName.error),
                Entry(lastName, stateLastName.error),
                Entry(birthDate, stateBirthDate.error),
                Entry(email, stateEmail.error),
                Entry(location, stateLocation.error),
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (lastName.isNotEmpty() && !stateLastName.error.isValid()) {
        if (stateLastName.error == LoginError.NameError.Length) {
            ErrorText(error = stringResource(R.string.create_account_last_name_error_length))
        } else if (stateLastName.error == LoginError.NameError.InvalidCharacters) {
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
                    .size(ForzenbookTheme.dimens.iconSizeSmall)
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
    if (birthDate.isNotEmpty() && !stateBirthDate.error.isValid()) {
        ErrorText(error = stringResource(R.string.create_account_birth_date_error))
    }
    InputField(
        label = stringResource(R.string.create_account_email_prompt),
        value = email,
        onValueChange = {
            email = it
            onTextChange(
                Entry(firstName, stateFirstName.error),
                Entry(lastName, stateLastName.error),
                Entry(birthDate, stateBirthDate.error),
                Entry(email, stateEmail.error),
                Entry(location, stateLocation.error),
            )
        },
        imeAction = ImeAction.Next,
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    )
    if (email.isNotEmpty() && !stateEmail.error.isValid()) {
        if (stateEmail.error == LoginError.EmailError.Length) {
            ErrorText(error = stringResource(R.string.login_email_error_length))
        } else if (stateEmail.error == LoginError.EmailError.InvalidFormat) {
            ErrorText(error = stringResource(R.string.login_email_error_format))
        }
    }
    InputField(
        label = stringResource(R.string.create_account_location_prompt),
        value = location,
        onValueChange = {
            location = it
            onTextChange(
                Entry(firstName, stateFirstName.error),
                Entry(lastName, stateLastName.error),
                Entry(birthDate, stateBirthDate.error),
                Entry(email, stateEmail.error),
                Entry(location, stateLocation.error),
            )
        },
        imeAction = ImeAction.Done,
        onDone = {
            keyboardController?.hide()
            if (stateEmail.error.isValid() && stateBirthDate.error.isValid() && stateLocation.error.isValid() && stateLastName.error.isValid() && stateFirstName.error.isValid()) {
                onSubmission()
            }
        }
    )
    if (location.isNotEmpty() && !stateLocation.error.isValid()) {
        if (stateLocation.error == LoginError.LocationError.Length) {
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
            enabled = (stateEmail.error.isValid() && stateBirthDate.error.isValid() && stateLocation.error.isValid() && stateLastName.error.isValid() && stateFirstName.error.isValid())
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
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        var selectedYear: Int = currentYear
        var selectedMonth: Int = currentMonth
        var selectedDay: Int = currentDay
        if (birthDate.isNotEmpty()) {
            val split = birthDate.split("-")
            selectedYear = split[2].toInt()
            selectedMonth = split[0].toInt() - 1
            selectedDay = split[1].toInt()
        }
        LaunchedEffect(Unit) {
            DatePickerDialog(
                context,
                R.style.MySpinnerDatePickerStyle,
                { _: DatePicker, year: Int, month: Int, day: Int ->
                    birthDate = stringDate(month + 1, day, year, context)
                    onTextChange(
                        Entry(firstName, stateFirstName.error),
                        Entry(lastName, stateLastName.error),
                        Entry(birthDate, stateBirthDate.error),
                        Entry(email, stateEmail.error),
                        Entry(location, stateLocation.error),
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

private fun stringDate(month: Int, day: Int, year: Int, context: Context): String {
    val date =
        context.getString(R.string.create_account_date, month, day, year).split("-")
    return date.joinToString("-") { it.leftPad() }
}

private fun String.leftPad(): String {
    return if (this.length < 2) {
        "0$this"
    } else this
}
