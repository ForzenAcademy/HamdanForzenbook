package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.R
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountRequestUseCase
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountResult
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase
import com.hamdan.forzenbook.domain.usecase.login.LoginRequestValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenUseCase: LoginGetTokenUseCase,
    private val requestValidationCode: LoginRequestValidationUseCase,
    private val createAccountRequestUseCase: CreateAccountRequestUseCase
) : ViewModel() {
    data class LoginState(
        val email: Entry = Entry("", true),
        val code: Entry = Entry("", true),
        val showInfoDialog: Boolean = false,
        val inputtingCode: Boolean = false,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

    data class CreateAccountState(
        val errorId: Int? = null,
        val firstName: Entry = Entry("", true),
        val lastName: Entry = Entry("", true),
        val birthDay: Entry = Entry("", true),
        val email: Entry = Entry("", true),
        val location: Entry = Entry("", true),
        val isLoading: Boolean = false,
    )

    private var _createAccountState: MutableState<CreateAccountState> =
        mutableStateOf(CreateAccountState())
    val createAccountState: MutableState<CreateAccountState>
        get() = _createAccountState

    private var _loginState: MutableState<LoginState> =
        // TODO remove this when Database for login token is setup (FA-84)
        mutableStateOf(LoginState(email = Entry("Hamdan.mobeen@Gmail.com", false)))
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
                // TODO remove delay when login flow fully complete with UI (FA-80 maybe)
                delay(1000)
                try {
                    getTokenUseCase(email.text, code.text)
                    // TODO here they would be considered succesful
                    // send off to where the user needs to see next, that location will retrieve token from DB
                    loginState.value = loginState.value.copy(
                        email = Entry(text = "", error = false),
                        code = Entry(text = "", error = false),
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
                // TODO remove delay when login flow fully complete with UI (FA-80 maybe)
                delay(1000)
                try {
                    requestValidationCode(it.email.text)
                    loginState.value = loginState.value.copy(
                        inputtingCode = true,
                        showInfoDialog = true,
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
                // TODO remove delay when login flow fully complete (FA-80 maybe)
                delay(1000)
                val result = createAccountRequestUseCase(
                    firstName = it.firstName.text,
                    lastName = it.lastName.text,
                    birthDay = it.birthDay.text,
                    email = it.email.text,
                    location = it.location.text,
                )
                when (result) {
                    CreateAccountResult.CREATE_SUCCESS -> {
                        createAccountState.value = createAccountState.value.copy()
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
    val error: Boolean
)
