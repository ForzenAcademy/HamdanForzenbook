package com.hamdan.forzenbook.createaccount.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.core.stringForm
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountEntrys
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.view.CreateUiComposeState
import com.hamdan.forzenbook.ui.core.R
import kotlinx.coroutines.launch

abstract class BaseCreateAccountViewModel(
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
) : ViewModel() {
    data class CreateAccountState(
        val errorId: Int? = null,
        val firstName: Entry = Entry("", LoginError.NameError.None),
        val lastName: Entry = Entry("", LoginError.NameError.None),
        val birthDay: Entry = Entry("", LoginError.BirthDateError.None),
        val email: Entry = Entry("", LoginError.EmailError.None),
        val location: Entry = Entry("", LoginError.LocationError.None),
        val isDateDialogOpen: Boolean = false,
        val isLoading: Boolean = false,
    )

    protected abstract var createAccountState: CreateAccountState

    var onAccountCreateSuccess: (() -> Unit)? = null

    fun createAccountDateDialogClicked() {
        createAccountShowDateDialog()
    }

    private fun createAccountShowDateDialog() {
        createAccountState = createAccountState.copy(isDateDialogOpen = true)
    }

    fun createAccountDateDialogSubmitClicked() {
        closeAccountShowDateDialog()
    }

    fun createAccountDateDialogDismiss() {
        closeAccountShowDateDialog()
    }

    private fun closeAccountShowDateDialog() {
        createAccountState = createAccountState.copy(isDateDialogOpen = false)
    }

    private fun createAccountNormalView() {
        createAccountState = createAccountState.copy(errorId = null)
    }

    fun createAccountDismissErrorClicked() {
        createAccountNormalView()
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
                createAccountState.copy(
                    firstName = firstName,
                    lastName = lastName,
                    birthDay = birthDay,
                    email = email,
                    location = location,
                ).toCreateAccountEntrys()
            )
        createAccountState = createAccountState.copy(
            firstName = stringStates.firstName,
            lastName = stringStates.lastName,
            birthDay = stringStates.birthDay,
            email = stringStates.email,
            location = stringStates.location,
        )
    }

    fun createAccount() {
        viewModelScope.launch {
            createAccountState.let {
                createAccountState = createAccountState.copy(
                    isLoading = true,
                )
                val split = it.birthDay.text.split("-")
                // convert date back to a readable format for the sql on the server
                val actualDate = "${split[2]}-${split[0]}-${split[1]}"
                val result = createAccountUseCase(
                    firstName = it.firstName.text,
                    lastName = it.lastName.text,
                    birthDay = actualDate,
                    email = it.email.text,
                    location = it.location.text,
                )
                when (result) {
                    CreateAccountResult.CREATE_SUCCESS -> {
                        createAccountState = createAccountState.copy(
                            firstName = Entry(text = "", error = LoginError.NameError.Length),
                            lastName = Entry(text = "", error = LoginError.NameError.Length),
                            birthDay = Entry(text = "", error = LoginError.BirthDateError.TooYoung),
                            email = Entry(text = "", error = LoginError.EmailError.Valid),
                            location = Entry(text = "", error = LoginError.LocationError.Length),
                            isLoading = false
                        )
                        // send to login page
                        onAccountCreateSuccess?.invoke()
                    }
                    CreateAccountResult.CREATE_EXISTS -> {
                        createAccountState = createAccountState.copy(
                            errorId = R.string.create_account_error_user_exists,
                            isLoading = false,
                        )
                    }
                    CreateAccountResult.CREATE_FAILURE -> {
                        createAccountState = createAccountState.copy(
                            errorId = R.string.create_account_error_generic,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }
}

fun BaseCreateAccountViewModel.CreateAccountState.toCreateAccountEntrys(): CreateAccountEntrys =
    CreateAccountEntrys(
        firstName = this.firstName,
        lastName = this.lastName,
        birthDay = this.birthDay,
        email = this.email,
        location = this.location
    )

fun BaseCreateAccountViewModel.CreateAccountState.toCreateAccountUiState(): CreateUiComposeState =
    CreateUiComposeState(
        errorId = this.errorId,
        firstName = this.firstName,
        lastName = this.lastName,
        birthDay = this.birthDay,
        email = this.email,
        location = this.location,
        isDateDialogOpen = this.isDateDialogOpen,
        isLoading = this.isLoading
    )

fun BaseCreateAccountViewModel.CreateAccountState.stringForm(): String {
    val first: LoginError = this.firstName.error as LoginError
    val last: LoginError = this.lastName.error as LoginError
    val birth: LoginError = this.birthDay.error as LoginError
    val email: LoginError = this.email.error as LoginError
    val location: LoginError = this.location.error as LoginError
    return "text: ${this.firstName.text}, ${first.stringForm()} \n text: ${this.lastName.text}, ${last.stringForm()} " +
        "text: ${this.birthDay.text}, ${birth.stringForm()} \n text: ${this.email.text}, ${email.stringForm()} " +
        "text: ${this.location.text}, ${location.stringForm()}" +
        "\n errorId: ${this.errorId}" +
        "\n dialogOpen: ${this.isDateDialogOpen}" +
        "\n loading: ${this.isLoading}"
}
