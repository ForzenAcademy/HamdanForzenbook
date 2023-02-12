package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountEntrys
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginEntrys
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginStringValidationUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
    private val loginStringValidationUseCase: LoginStringValidationUseCase,
    private val getTokenFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val getTokenFromDatabaseUseCase: LoginGetStoredCredentialsUseCase,
    private val requestValidationCode: LoginValidationUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
) : ViewModel() {
    data class LoginState(
        val email: Entry = Entry("", LoginError.EmailError.None),
        val code: Entry = Entry("", LoginError.CodeError.None),
        val showInfoDialog: Boolean = false,
        val inputtingCode: Boolean = false,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

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
        val stringStates = loginStringValidationUseCase(loginState.value.toLoginEntrys())
        loginState.value =
            loginState.value.copy(email = stringStates.email, code = stringStates.code)
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
        val stringStates =
            createAccountValidationUseCase(createAccountState.value.toCreateAccountEntrys())
        createAccountState.value = createAccountState.value.copy(
            firstName = stringStates.firstName,
            lastName = stringStates.lastName,
            birthDay = stringStates.birthDay,
            email = stringStates.email,
            location = stringStates.location,
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

fun LoginViewModel.CreateAccountState.toCreateAccountEntrys(): CreateAccountEntrys =
    CreateAccountEntrys(
        firstName = this.firstName,
        lastName = this.lastName,
        birthDay = this.birthDay,
        email = this.email,
        location = this.location
    )

fun LoginViewModel.LoginState.toLoginEntrys(): LoginEntrys =
    LoginEntrys(
        email = this.email,
        code = this.code,
    )
