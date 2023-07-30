package com.hamdan.forzenbook.createaccount.core.viewmodel

import androidx.lifecycle.ViewModel
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountEntrys
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase

// the createAccountUseCase is used individualy in the correct legacy or current viewmodel
abstract class BaseCreateAccountViewModel(
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
) : ViewModel() {
    data class CreateAccountData(
        val firstName: Entry = Entry("", EntryError.NameError.None),
        val lastName: Entry = Entry("", EntryError.NameError.None),
        val birthDay: Entry = Entry("", EntryError.BirthDateError.None),
        val email: Entry = Entry("", EntryError.EmailError.None),
        val location: Entry = Entry("", EntryError.LocationError.None),
        val isDateDialogOpen: Boolean = false,
    )

    sealed interface CreateAccountState {
        val createAccountData: CreateAccountData?

        data class Content(override val createAccountData: CreateAccountData) : CreateAccountState
        data class Error(
            val errorId: Int,
            override val createAccountData: CreateAccountData? = null
        ) : CreateAccountState

        data class Loading(override val createAccountData: CreateAccountData? = null) :
            CreateAccountState

        data class AccountCreated(override val createAccountData: CreateAccountData? = null) :
            CreateAccountState
    }

    protected abstract var createAccountState: CreateAccountState

    fun createAccountDateDialogClicked() {
        val currentState = createAccountState
        if (currentState is CreateAccountState.Content) {
            createAccountState = CreateAccountState.Content(
                createAccountState.getContent().createAccountData.copy(isDateDialogOpen = true)
            )
        } else throw StateException()
    }

    fun createAccountDateDialogSubmitClicked() {
        val currentState = createAccountState
        if (currentState is CreateAccountState.Content) {
            createAccountState = CreateAccountState.Content(
                createAccountState.getContent().createAccountData.copy(isDateDialogOpen = false)
            )
        } else throw StateException()
    }

    fun createAccountDateDialogDismiss() {
        val currentState = createAccountState
        if (currentState is CreateAccountState.Content) {
            createAccountState = CreateAccountState.Content(
                createAccountState.getContent().createAccountData.copy(isDateDialogOpen = false)
            )
        } else throw StateException()
    }

    fun navigateUpPressed() {
        val currentState = createAccountState
        if (currentState is CreateAccountState.AccountCreated) {
            createAccountState = CreateAccountState.Content(CreateAccountData())
        }
    }

    fun createAccountDismissErrorClicked() {
        val currentState = createAccountState
        if (currentState is CreateAccountState.Error) {
            createAccountState =
                CreateAccountState.Content(currentState.createAccountData ?: CreateAccountData())
        }
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
                    createAccountState.getContent().createAccountData.copy(
                        firstName = firstName,
                        lastName = lastName,
                        birthDay = birthDay,
                        email = email,
                        location = location,
                    )
                ).toCreateAccountEntries()
            )
        createAccountState = CreateAccountState.Content(
            createAccountState.getContent().createAccountData.copy(
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

fun BaseCreateAccountViewModel.CreateAccountState.toCreateAccountEntries(): CreateAccountEntrys {
    (this as BaseCreateAccountViewModel.CreateAccountState.Content).createAccountData.apply {
        return CreateAccountEntrys(
            firstName = this.firstName,
            lastName = this.lastName,
            birthDay = this.birthDay,
            email = this.email,
            location = this.location,
        )
    }
}
