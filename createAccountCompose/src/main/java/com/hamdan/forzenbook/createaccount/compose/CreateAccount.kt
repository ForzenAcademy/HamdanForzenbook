package com.hamdan.forzenbook.createaccount.compose

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composables.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composables.ErrorText
import com.hamdan.forzenbook.compose.core.composables.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composables.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composables.InputField
import com.hamdan.forzenbook.compose.core.composables.LoadingButton
import com.hamdan.forzenbook.compose.core.composables.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.compose.core.composables.SubmitButton
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.dimens
import com.hamdan.forzenbook.compose.core.theme.ForzenbookTheme.typography
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.stringDate
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent
import com.hamdan.forzenbook.ui.core.R
import java.util.Calendar

private const val ONE_LINE = 1

@Composable
fun CreateAccountContent(
    state: BaseCreateAccountViewModel.CreateAccountState,
    onErrorDismiss: () -> Unit,
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit,
    onDateFieldClick: () -> Unit,
    onDateSubmission: () -> Unit,
    onDateDismiss: () -> Unit,
    onSubmission: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val navigator = LocalNavController.current
    when (state) {
        BaseCreateAccountViewModel.CreateAccountState.AccountCreated -> {
            ContentWrapper {}
            LaunchedEffect(Unit) {
                navigator?.navigateUp()
                onNavigateUp()
            }
        }
        is BaseCreateAccountViewModel.CreateAccountState.Content -> {
            ContentWrapper {
                MainContent(
                    stateFirstName = state.getContent().createAccountContent.firstName,
                    stateLastName = state.getContent().createAccountContent.lastName,
                    stateBirthDate = state.getContent().createAccountContent.birthDay,
                    stateEmail = state.getContent().createAccountContent.email,
                    stateLocation = state.getContent().createAccountContent.location,
                    isDateDialogOpen = state.getContent().createAccountContent.isDateDialogOpen,
                    onTextChange = onTextChange,
                    onDateSubmission = onDateSubmission,
                    onDateDismiss = onDateDismiss,
                    onSubmission = onSubmission,
                    onDateFieldClick = onDateFieldClick,
                )
            }
        }
        is BaseCreateAccountViewModel.CreateAccountState.Error -> {
            ContentWrapper {
                ErrorContent(state.errorId, onErrorDismiss)
            }
        }
        BaseCreateAccountViewModel.CreateAccountState.Loading -> {
            ContentWrapper {
                LoadingContent()
            }
            PreventScreenActionsDuringLoad()
        }
        else -> {
            throw Exception("Illegal unknown state")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentWrapper(content: @Composable ColumnScope.() -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ForzenbookTopAppBar(topText = stringResource(R.string.top_bar_text_create_account)) },
    ) { padding ->
        BackgroundColumn(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    stateFirstName: Entry = Entry("", EntryError.NameError.None),
    stateLastName: Entry = Entry("", EntryError.NameError.None),
    stateBirthDate: Entry = Entry("", EntryError.BirthDateError.None),
    stateEmail: Entry = Entry("", EntryError.EmailError.None),
    stateLocation: Entry = Entry("", EntryError.LocationError.None),
    isDateDialogOpen: Boolean = false,
    isLoading: Boolean = false,
    onTextChange: (Entry, Entry, Entry, Entry, Entry) -> Unit = { _, _, _, _, _ -> },
    onDateSubmission: () -> Unit = {},
    onDateDismiss: () -> Unit = {},
    onSubmission: () -> Unit = {},
    onDateFieldClick: () -> Unit = {},
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
        if (stateFirstName.error == EntryError.NameError.Length) {
            ErrorText(error = stringResource(R.string.create_account_first_name_error_length))
        } else if (stateFirstName.error == EntryError.NameError.InvalidCharacters) {
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
        if (stateLastName.error == EntryError.NameError.Length) {
            ErrorText(error = stringResource(R.string.create_account_last_name_error_length))
        } else if (stateLastName.error == EntryError.NameError.InvalidCharacters) {
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
                horizontal = ForzenbookTheme.dimens.grid.x10,
                vertical = ForzenbookTheme.dimens.grid.x2
            )
            .clickable {
                onDateFieldClick()
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.calendar_icon),
                modifier = Modifier
                    .size(ForzenbookTheme.dimens.imageSizes.small)
            )
        },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            disabledTextColor = ForzenbookTheme.colors.colors.onBackground,
            disabledPlaceholderColor = ForzenbookTheme.colors.colors.onBackground,
            disabledLabelColor = ForzenbookTheme.colors.colors.onBackground,
            // For Icons
            disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
            disabledTrailingIconColor = MaterialTheme.colorScheme.secondary
        ),
        textStyle = ForzenbookTheme.typography.headlineMedium,
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
        if (stateEmail.error == EntryError.EmailError.Length) {
            ErrorText(error = stringResource(R.string.login_email_error_length))
        } else if (stateEmail.error == EntryError.EmailError.InvalidFormat) {
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
        if (stateLocation.error == EntryError.LocationError.Length) {
            ErrorText(error = stringResource(R.string.create_account_location_error))
        }
    }
    Spacer(modifier = Modifier.height(ForzenbookTheme.dimens.grid.x3))
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
    val context = LocalContext.current
    if (isDateDialogOpen) {
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

@Composable
private fun ErrorContent(errorId: Int, onErrorDismiss: () -> Unit) {
    MainContent()
    ForzenbookDialog(
        title = stringResource(R.string.create_account_error_title),
        body = stringResource(errorId),
        buttonText = stringResource(id = R.string.generic_dialog_confirm),
        onDismiss = onErrorDismiss
    )
}

@Composable
private fun LoadingContent() {
    MainContent(isLoading = true)
}
