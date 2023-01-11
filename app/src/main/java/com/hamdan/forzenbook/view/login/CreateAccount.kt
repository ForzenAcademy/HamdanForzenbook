package com.hamdan.forzenbook.view.login

import android.app.DatePickerDialog
import android.text.TextUtils
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import com.hamdan.forzenbook.theme.PaddingValues
import com.hamdan.forzenbook.view.composeutils.DimBackgroundLoad
import com.hamdan.forzenbook.view.composeutils.InputField
import com.hamdan.forzenbook.view.composeutils.LoginBackgroundColumn
import com.hamdan.forzenbook.view.composeutils.LoginTopBar
import com.hamdan.forzenbook.view.composeutils.SubmitButton
import com.hamdan.forzenbook.view.composeutils.validateEmail
import com.hamdan.forzenbook.view.login.LoginSharedConstants.EMAIL_LENGTH_LIMIT
import com.hamdan.forzenbook.viewmodels.Entry
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import java.util.Calendar

private const val AGE_MINIMUM = 13
private const val NAME_LENGTH_LIMIT = 20
private const val LOCATION_LENGTH_LIMIT = 64

@Composable
fun CreateAccountContent(
    state: LoginViewModel.CreateAccountState,
    onErrorDismiss: () -> Unit,
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit,
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
                onTextChange = onTextChange,
                onSubmission = onSubmission,
                onErrorDismiss = onErrorDismiss,
            )
        }
        if (state.isLoading) {
            DimBackgroundLoad()
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
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit,
    onSubmission: () -> Unit,
    onErrorDismiss: () -> Unit,
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

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    var selectedYear: Int = currentYear
    var selectedMonth: Int = currentMonth
    var selectedDay: Int = currentDay
    if (birthDate.isNotEmpty()) {
        val split = birthDate.split("-")
        selectedYear = split[0].toInt()
        selectedMonth = split[1].toInt()
        selectedDay = split[2].toInt()
    }
    val datePicker =
        DatePickerDialog(
            context,
            R.style.MySpinnerDatePickerStyle,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val actualMonth = month + 1
                birthDate = context.getString(R.string.create_account_date, year, actualMonth, day)
                birthError = (
                    (currentYear - year < AGE_MINIMUM) ||
                        ((currentYear - year == AGE_MINIMUM && currentMonth - actualMonth < 0)) ||
                        ((currentYear - year == AGE_MINIMUM && currentMonth - actualMonth == 0 && currentDay - day < 0))
                    )
                onTextChange(
                    Entry(firstName, firstNameError),
                    Entry(lastName, lastNameError),
                    Entry(birthDate, birthError),
                    Entry(email, emailError),
                    Entry(location, locationError),
                )
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
    if (firstNameError && firstName.isNotEmpty()) {
        Text(text = stringResource(R.string.create_account_first_name_error))
    }
    InputField(
        label = stringResource(R.string.create_account_last_name_prompt),
        value = lastName,
        onValueChange = {
            lastName = it
            lastNameError = lastName.length > NAME_LENGTH_LIMIT
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
    if (lastNameError && lastName.isNotEmpty()) {
        Text(text = stringResource(R.string.create_account_last_name_error))
    }
    TextField(
        value = if (birthDate == "") stringResource(R.string.create_account_birth_date_prompt) else birthDate,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PaddingValues.largePad_4,
                vertical = PaddingValues.smallPad_2
            )
            .clickable {
                datePicker.show()
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
        singleLine = true
    )
    if (birthError && birthDate.isNotEmpty()) {
        Text(text = stringResource(R.string.create_account_birth_date_error))
    }
    InputField(
        label = stringResource(R.string.create_account_email_prompt),
        value = email,
        onValueChange = {
            email = it
            emailError =
                email.length > EMAIL_LENGTH_LIMIT ||
                !TextUtils.isEmpty(email) && !validateEmail(email)
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
    if (emailError && email.isNotEmpty()) {
        Text(text = stringResource(R.string.create_account_email_error))
    }
    InputField(
        label = stringResource(R.string.create_account_location_prompt),
        value = location,
        onValueChange = {
            location = it
            locationError = location.length > LOCATION_LENGTH_LIMIT
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
            if (!(emailError || birthError || locationError || lastNameError || firstNameError)) {
                onSubmission()
            }
        }
    )
    if (locationError && location.isNotEmpty()) {
        Text(text = stringResource(R.string.create_account_location_error))
    }
    Spacer(modifier = Modifier.height(com.hamdan.forzenbook.theme.PaddingValues.smallPad_3))
    SubmitButton(
        onSubmission = onSubmission,
        label = stringResource(R.string.create_account_submit),
        enabled = !(emailError || birthError || locationError || firstNameError || lastNameError)
    )
    Spacer(modifier = Modifier.height(60.dp))

    // TODO In FA 82 deal with color scheming as well as potential ellipsize / wrapping
    if (stateError != null) {
        AlertDialog(
            onDismissRequest = { onErrorDismiss() },
            title = {
                Text(text = stringResource(R.string.create_account_error_title))
            },
            text = {
                Text(text = stringResource(stateError))
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.create_account_confirm_error),
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
}
