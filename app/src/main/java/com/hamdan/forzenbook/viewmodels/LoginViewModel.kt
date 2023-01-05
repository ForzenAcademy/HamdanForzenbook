package com.hamdan.forzenbook.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountRequestUseCase
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenUseCase: LoginGetTokenUseCase,
    private val createAccountRequestUseCase: CreateAccountRequestUseCase
) : ViewModel() {

    sealed interface LoginState {
        object Error : LoginState

        data class Loading(
            val email: String = "",
            val emailError: Boolean = true,
        ) : LoginState

        data class UserInputting(
            val email: String = "",
            val emailError: Boolean = true,
        ) : LoginState
    }

    sealed interface CreateAccountState {
        object Loading : CreateAccountState
        object Error : CreateAccountState

        data class UserInputting(
            val firstName: String = "",
            val lastName: String = "",
            val birthDay: String = "",
            val email: String = "",
            val location: String = "",
            val firstError: Boolean = true,
            val lastError: Boolean = true,
            val birthError: Boolean = true,
            val emailError: Boolean = true,
            val locationError: Boolean = true,
        ) : CreateAccountState
    }

    private var _createAccountState: MutableState<CreateAccountState> =
        mutableStateOf(CreateAccountState.UserInputting())
    val createAccountState: MutableState<CreateAccountState>
        get() = _createAccountState

    private var _loginState: MutableState<LoginState> =
        mutableStateOf(LoginState.UserInputting())
    val loginState: MutableState<LoginState>
        get() = _loginState

    lateinit var onAccountCreateSuccess: () -> Unit

    fun updateLoginTexts(
        email: String,
        emailError: Boolean,
    ) {
        loginState.value = LoginState.UserInputting(email, emailError)
    }

    fun updateCreateAccountTextAndErrors(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String,
        firstError: Boolean,
        lastError: Boolean,
        birthError: Boolean,
        emailError: Boolean,
        locationError: Boolean,
    ) {
        createAccountState.value = CreateAccountState.UserInputting(
            firstName, lastName, birthDay, email, location,
            firstError, lastError, birthError, emailError, locationError
        )
    }

    fun resetCreateAccountState() {
        createAccountState.value = CreateAccountState.UserInputting()
    }

    fun resetLoginState() {
        loginState.value = LoginState.UserInputting()
    }

    fun submitLogin() {
        viewModelScope.launch {
            if (loginState.value is LoginState.UserInputting) {
                (loginState.value as LoginState.UserInputting).let {
                    val email = it.email
                    val emailError = it.emailError
                    loginState.value =
                        LoginState.Loading(email, emailError)
                    delay(1000)
                    val token: String? = try {
                        getTokenUseCase(email)?.token
                    } catch (e: Exception) {
                        null
                    }
                    //we will want to replace this if section potentially
                    if (token == null) {
                        loginState.value = LoginState.Error
                        //we may also want to show user the error
                    } else {
                        loginState.value =
                            LoginState.UserInputting(email, emailError)
                        Log.v("Hamdan", "We got the token it is: $token")
                    }

                }
            } else {
                //throw an error here
                Log.v("Hamdan", "There was a major issue")
            }
        }
    }

    fun createAccount() {
        viewModelScope.launch {
            if (createAccountState.value is CreateAccountState.UserInputting) {
                (createAccountState.value as CreateAccountState.UserInputting).let {
                    val firstName: String = it.firstName
                    val lastName: String = it.lastName
                    val birthDay: String = it.birthDay
                    val email: String = it.email
                    val location: String = it.location
                    createAccountState.value = CreateAccountState.Loading
                    delay(1000)
                    val code = try {
                        createAccountRequestUseCase(
                            firstName,
                            lastName,
                            birthDay,
                            email,
                            location
                        )
                    } catch (e: Exception) {
                        0
                    }
                    if (code in 200..299) {
                        //send to login page
                        createAccountState.value = CreateAccountState.UserInputting()
                        onAccountCreateSuccess()
                    } else if (code < 100) {
                        createAccountState.value = CreateAccountState.Error
                    } else {
                        createAccountState.value = CreateAccountState.Error
                        //maybe we want to prompt them with a dialog on what error was?
                    }
                }
            }
        }
    }
}