package com.hamdan.forzenbook.createaccount.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.composewidgets.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.compose.core.composewidgets.ScaffoldWrapper
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent

@Composable
fun CreateAccount(
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
            ScaffoldWrapper {}
            LaunchedEffect(Unit) {
                navigator?.navigateUp()
                onNavigateUp()
            }
        }

        is BaseCreateAccountViewModel.CreateAccountState.Content -> {
            ScaffoldWrapper {
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
            ScaffoldWrapper {
                ErrorContent(state.errorId, onErrorDismiss)
            }
        }

        is BaseCreateAccountViewModel.CreateAccountState.Loading -> {
            ScaffoldWrapper {
                LoadingContent()
            }
            PreventScreenActionsDuringLoad()
        }

        else -> {
            throw StateException()
        }
    }
}
