package com.hamdan.forzenbook.createaccount.core.viewmodel

import androidx.lifecycle.ViewModel
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountEntrys
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase

// the createAccountUseCase is used individualy in the correct legacy or current viewmodel
abstract class BaseCreateAccountViewModel(
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
) : ViewModel() {
    data class CreateAccountContent(
        val firstName: Entry = Entry("", EntryError.NameError.None),
        val lastName: Entry = Entry("", EntryError.NameError.None),
        val birthDay: Entry = Entry("", EntryError.BirthDateError.None),
        val email: Entry = Entry("", EntryError.EmailError.None),
        val location: Entry = Entry("", EntryError.LocationError.None),
        val isDateDialogOpen: Boolean = false,
    )

    sealed interface CreateAccountState {
        data class Content(val createAccountContent: CreateAccountContent) : CreateAccountState
        data class Error(val errorId: Int) : CreateAccountState
        object Loading : CreateAccountState
        object AccountCreated : CreateAccountState
    }

    protected abstract var createAccountState: CreateAccountState

    fun createAccountDateDialogClicked() = createAccountShowDateDialog()

    private fun createAccountShowDateDialog() {
        createAccountState = CreateAccountState.Content(
            createAccountState.getContent().createAccountContent.copy(isDateDialogOpen = true)
        )
    }

    fun createAccountDateDialogSubmitClicked() = closeAccountShowDateDialog()

    fun createAccountDateDialogDismiss() = closeAccountShowDateDialog()

    fun navigateUpPressed() {
        if (createAccountState is CreateAccountState.AccountCreated) {
            createAccountState = CreateAccountState.Content(CreateAccountContent())
        }
    }

    private fun closeAccountShowDateDialog() {
        createAccountState = CreateAccountState.Content(
            createAccountState.getContent().createAccountContent.copy(isDateDialogOpen = false)
        )
    }

    fun createAccountDismissErrorClicked() {
        createAccountState = CreateAccountState.Content(CreateAccountContent())
    }

    fun updateCreateAccountTextAndErrors(
        firstName: Entry,
        lastName: Entry,
        birthDay: Entry,
        email: Entry,
        location: Entry,
    ) {
        val stringStates =
            createAccountValidationUseCase(
                CreateAccountState.Content(
                    createAccountState.getContent().createAccountContent.copy(
                        firstName = firstName,
                        lastName = lastName,
                        birthDay = birthDay,
                        email = email,
                        location = location,
                    )
                ).toCreateAccountEntrys()
            )
        createAccountState = CreateAccountState.Content(
            createAccountState.getContent().createAccountContent.copy(
                firstName = stringStates.firstName,
                lastName = stringStates.lastName,
                birthDay = stringStates.birthDay,
                email = stringStates.email,
                location = stringStates.location,
            )
        )
    }
}

fun BaseCreateAccountViewModel.CreateAccountState.getContent() =
    this as BaseCreateAccountViewModel.CreateAccountState.Content

fun BaseCreateAccountViewModel.CreateAccountState.getError() =
    this as BaseCreateAccountViewModel.CreateAccountState.Error

fun BaseCreateAccountViewModel.CreateAccountState.toCreateAccountEntrys(): CreateAccountEntrys {
    (this as BaseCreateAccountViewModel.CreateAccountState.Content).createAccountContent.apply {
        return CreateAccountEntrys(
            firstName = this.firstName,
            lastName = this.lastName,
            birthDay = this.birthDay,
            email = this.email,
            location = this.location,
        )
    }
}
