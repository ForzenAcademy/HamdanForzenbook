package com.hamdan.forzenbook.applegacy.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountEntrys
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LegacyCreateAccountViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
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

    // TODO REMOVE THIS FUNCTION WHEN WORKING ON Legacy Create Account FA-102
    fun test() {
        Log.v("Hamdan", "Hello")
    }

    private var errorId: Int? = null
    private var firstName: Entry = Entry("", LoginError.NameError.None)
    private var lastName: Entry = Entry("", LoginError.NameError.None)
    private var birthDay: Entry = Entry("", LoginError.BirthDateError.None)
    private var email: Entry = Entry("", LoginError.EmailError.None)
    private var location: Entry = Entry("", LoginError.LocationError.None)
    private var isDateDialogOpen: Boolean = false
    private var isLoading: Boolean = false

    var onCreateAccountUpdate: ((CreateAccountState) -> Unit)? = null
    lateinit var onAccountCreateSuccess: () -> Unit

    private fun updateCreateAccountState() {
        onCreateAccountUpdate?.invoke(
            CreateAccountState(
                errorId = errorId,
                firstName = firstName,
                lastName = lastName,
                birthDay = birthDay,
                email = email,
                location = location,
                isDateDialogOpen = isDateDialogOpen,
                isLoading = isLoading
            )
        )
    }

    fun updateCreateAccountTextAndErrors(
        inFirstName: String,
        inLastName: String,
        inBirthDay: String,
        inEmail: String,
        inLocation: String,
    ) {
        firstName = firstName.copy(text = inFirstName)
        lastName = lastName.copy(text = inLastName)
        birthDay = birthDay.copy(text = inBirthDay)
        email = email.copy(text = inEmail)
        location = location.copy(text = inLocation)
        val stringStates =
            createAccountValidationUseCase(
                CreateAccountEntrys(
                    firstName = firstName,
                    lastName = lastName,
                    birthDay = birthDay,
                    email = email,
                    location = location
                )
            )
        firstName = stringStates.firstName
        lastName = stringStates.lastName
        birthDay = stringStates.birthDay
        email = stringStates.email
        location = stringStates.location
        updateCreateAccountState()
    }

    fun createAccountDateDialogClicked() {
        createAccountShowDateDialog()
    }

    private fun createAccountShowDateDialog() {
        isDateDialogOpen = true
        updateCreateAccountState()
    }

    fun createAccountDateDialogSubmitClicked() {
        closeAccountShowDateDialog()
    }

    fun createAccountDateDialogDismiss() {
        closeAccountShowDateDialog()
    }

    private fun closeAccountShowDateDialog() {
        isDateDialogOpen = false
        updateCreateAccountState()
    }

    private fun createAccountNormalView() {
        errorId = null
        updateCreateAccountState()
    }

    fun createAccountDismissErrorClicked() {
        createAccountNormalView()
    }

    fun createAccount() {
        viewModelScope.launch {
            isLoading = true
            updateCreateAccountState()
            val split = birthDay.text.split("-")
            // convert date back to a readable format for the sql on the server
            val actualDate = "${split[2]}-${split[0]}-${split[1]}"
            val result = createAccountUseCase(
                firstName = firstName.text,
                lastName = lastName.text,
                birthDay = actualDate,
                email = email.text,
                location = location.text,
            )
            when (result) {
                CreateAccountResult.CREATE_SUCCESS -> {
                    errorId = null
                    firstName = Entry("", LoginError.NameError.None)
                    lastName = Entry("", LoginError.NameError.None)
                    birthDay = Entry("", LoginError.BirthDateError.None)
                    email = Entry("", LoginError.EmailError.None)
                    location = Entry("", LoginError.LocationError.None)
                    isDateDialogOpen = false
                    isLoading = false
                    updateCreateAccountState()
                    // send to login page
                    onAccountCreateSuccess()
                }
                CreateAccountResult.CREATE_EXISTS -> {
                    errorId = R.string.create_account_error_user_exists
                    isLoading = false
                    updateCreateAccountState()
                }
                CreateAccountResult.CREATE_FAILURE -> {
                    errorId = R.string.create_account_error_generic
                    isLoading = false
                    updateCreateAccountState()
                }
            }
        }
    }
}
