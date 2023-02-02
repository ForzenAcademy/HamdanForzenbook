package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountResult
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountUseCase
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetCredentialsFromNetwork
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseGetStoredCredentials
import com.hamdan.forzenbook.domain.usecase.login.LoginUseCaseValidation
import com.hamdan.forzenbook.view.login.LoginError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenFromNetworkUseCase: LoginUseCaseGetCredentialsFromNetwork,
    private val getTokenFromDatabaseUseCase: LoginUseCaseGetStoredCredentials,
    private val requestValidationCode: LoginUseCaseValidation,
    private val createAccountUseCase: CreateAccountUseCase,
) : ViewModel() {
    data class LoginState(
        val email: Entry = Entry("", LoginError.EmailError.Length),
        val code: Entry = Entry("", LoginError.CodeError.Length),
        val showInfoDialog: Boolean = false,
        val inputtingCode: Boolean = false,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

    data class CreateAccountState(
        val errorId: Int? = null,
        val firstName: Entry = Entry("", LoginError.NameError.Length),
        val lastName: Entry = Entry("", LoginError.NameError.Length),
        val birthDay: Entry = Entry("", LoginError.BirthDateError.TooYoung),
        val email: Entry = Entry("", LoginError.EmailError.Length),
        val location: Entry = Entry("", LoginError.LocationError.Length),
        val isDateDialogOpen: Boolean = false,
        val isLoading: Boolean = false,
    )

    private var _createAccountState: MutableState<CreateAccountState> =
        mutableStateOf(CreateAccountState())
    val createAccountState: MutableState<CreateAccountState>
        get() = _createAccountState

    private var _loginState: MutableState<LoginState> =
        mutableStateOf(LoginState(email = Entry("", LoginError.NameError.Length)))
    val loginState: MutableState<LoginState>
        get() = _loginState

    lateinit var onAccountCreateSuccess: () -> Unit

    fun updateLoginTexts(
        email: Entry,
        code: Entry,
        isInputtingCode: Boolean,
    ) {
        loginState.value = loginState.value.copy(
            email = email,
            code = code,
            inputtingCode = isInputtingCode,
        )
    }

    fun updateCreateAccountTextAndErrors(
        firstName: Entry,
        lastName: Entry,
        birthDay: Entry,
        email: Entry,
        location: Entry,
    ) {
        createAccountState.value = createAccountState.value.copy(
            firstName = firstName,
            lastName = lastName,
            birthDay = birthDay,
            email = email,
            location = location,
        )
    }

    fun createAccountDateDialogClicked() {
        createAccountShowDateDialog()
    }

    private fun createAccountShowDateDialog() {
        createAccountState.value = createAccountState.value.copy(isDateDialogOpen = true)
    }

    fun createAccountDateDialogSubmitClicked() {
        closeAccountShowDateDialog()
    }

    fun createAccountDateDialogDismiss() {
        closeAccountShowDateDialog()
    }

    private fun closeAccountShowDateDialog() {
        createAccountState.value = createAccountState.value.copy(isDateDialogOpen = false)
    }

    private fun createAccountNormalView() {
        createAccountState.value = createAccountState.value.copy(errorId = null)
    }

    fun createAccountDismissErrorClicked() {
        createAccountNormalView()
    }

    private fun loginNormalView() {
        loginState.value =
            loginState.value.copy(hasError = false, showInfoDialog = false, isLoading = false)
    }

    fun loginDismissErrorClicked() {
        loginNormalView()
    }

    fun loginDismissInfoClicked() {
        loginNormalView()
    }

    private fun submitLogin() {
        viewModelScope.launch {
            loginState.value.let {
                val email = it.email
                val code = it.code
                loginState.value =
                    loginState.value.copy(
                        email = email,
                        code = code,
                        inputtingCode = true,
                        isLoading = true,
                        hasError = false,
                    )
                try {
                    getTokenFromNetworkUseCase(email.text, code.text)
                    // TODO here they would be considered succesful
                    // send off to where the user needs to see next, that location will retrieve token from DB
                    loginState.value = loginState.value.copy(
                        email = Entry(text = "", error = LoginError.EmailError.Valid),
                        code = Entry(text = "", error = LoginError.CodeError.Length),
                        showInfoDialog = false,
                        inputtingCode = false,
                        isLoading = false,
                        hasError = false
                    )
                    // for now we will show a reseted page
                } catch (e: Exception) {
                    loginState.value = loginState.value.copy(
                        email = email,
                        code = code,
                        inputtingCode = true,
                        isLoading = false,
                        hasError = true,
                    )
                }
            }
        }
    }

    private fun requestLoginValidationCode() {
        viewModelScope.launch {
            loginState.value.let {
                loginState.value = loginState.value.copy(
                    isLoading = true,
                )
                try {
                    requestValidationCode(it.email.text)
                    loginState.value = loginState.value.copy(
                        inputtingCode = true,
                        showInfoDialog = true,
                        isLoading = false,
                    )
                } catch (e: Exception) {
                    loginState.value = loginState.value.copy(
                        inputtingCode = false,
                        isLoading = false,
                        hasError = true,
                    )
                }
            }
        }
    }

    fun createAccount() {
        viewModelScope.launch {
            createAccountState.value.let {
                createAccountState.value = createAccountState.value.copy(
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
                        createAccountState.value = createAccountState.value.copy(
                            firstName = Entry(text = "", error = LoginError.NameError.Length),
                            lastName = Entry(text = "", error = LoginError.NameError.Length),
                            birthDay = Entry(text = "", error = LoginError.BirthDateError.TooYoung),
                            email = Entry(text = "", error = LoginError.EmailError.Valid),
                            location = Entry(text = "", error = LoginError.LocationError.Length),
                            isLoading = false
                        )
                        // send to login page
                        onAccountCreateSuccess()
                    }
                    CreateAccountResult.CREATE_EXISTS -> {
                        createAccountState.value = createAccountState.value.copy(
                            errorId = R.string.create_account_error_user_exists,
                            isLoading = false,
                        )
                    }
                    CreateAccountResult.CREATE_FAILURE -> {
                        createAccountState.value = createAccountState.value.copy(
                            errorId = R.string.create_account_error_generic,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    fun loginClicked() {
        if (loginState.value.inputtingCode) {
            submitLogin()
        } else {
            requestLoginValidationCode()
        }
    }
}

data class Entry(
    val text: String,
    val error: LoginError
)
