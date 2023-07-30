package com.hamdan.forzenbook.createaccount.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.ErrorText
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookTopAppBar
import com.hamdan.forzenbook.compose.core.composewidgets.InputField
import com.hamdan.forzenbook.compose.core.composewidgets.LoadingButton
import com.hamdan.forzenbook.compose.core.composewidgets.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.compose.core.composewidgets.SubmitButton
import com.hamdan.forzenbook.compose.core.composewidgets.TitleText
import com.hamdan.forzenbook.compose.core.theme.additionalColors
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.core.datePickerDialog
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent
import com.hamdan.forzenbook.ui.core.R

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
        is BaseCreateAccountViewModel.CreateAccountState.AccountCreated -> {
            ContentWrapper {}
            LaunchedEffect(Unit) {
                navigator?.navigateUp()
                onNavigateUp()
            }
        }

        is BaseCreateAccountViewModel.CreateAccountState.Content -> {
            ContentWrapper {
                MainContent(
                    stateFirstName = state.getContent().createAccountData.firstName,
                    stateLastName = state.getContent().createAccountData.lastName,
                    stateBirthDate = state.getContent().createAccountData.birthDay,
                    stateEmail = state.getContent().createAccountData.email,
                    stateLocation = state.getContent().createAccountData.location,
                    isDateDialogOpen = state.getContent().createAccountData.isDateDialogOpen,
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

        is BaseCreateAccountViewModel.CreateAccountState.Loading -> {
            ContentWrapper {
                LoadingContent()
            }
            PreventScreenActionsDuringLoad()
        }

        else -> {
            throw StateException()
        }
    }
}

@Composable
private fun ContentWrapper(content: @Composable ColumnScope.() -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { ForzenbookTopAppBar(titleSection = { TitleText(text = stringResource(R.string.top_bar_text_create_account)) }) },
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
        modifier = Modifier.padding(top = MaterialTheme.dimens.grid.x2),
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
        label = {
            Text(
                text = stringResource(R.string.create_account_birth_date_prompt),
                style = MaterialTheme.typography.bodySmall,
            )
        },
        onValueChange = {},
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimens.grid.x10,
                vertical = MaterialTheme.dimens.grid.x2
            )
            .clickable {
                focusManager.clearFocus(true)
                onDateFieldClick()
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = stringResource(id = R.string.calendar_icon),
            )
        },
        enabled = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.additionalColors.inputFieldContainer,
            disabledTextColor = MaterialTheme.additionalColors.onInputFieldContainer,
            disabledPlaceholderColor = MaterialTheme.additionalColors.onInputFieldContainer,
            disabledLabelColor = MaterialTheme.additionalColors.onInputFieldContainer,
            // For Icons
            disabledTrailingIconColor = MaterialTheme.additionalColors.onInputFieldContainer,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
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
    Spacer(modifier = Modifier.height(MaterialTheme.dimens.grid.x3))
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
        LaunchedEffect(Unit) {
            datePickerDialog(
                context,
                birthDate,
                {
                    onTextChange(
                        Entry(firstName, stateFirstName.error),
                        Entry(lastName, stateLastName.error),
                        Entry(it, stateBirthDate.error),
                        Entry(email, stateEmail.error),
                        Entry(location, stateLocation.error),
                    )
                },
                onDateSubmission,
                onDateDismiss
            )
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
