package com.example.forzenbook.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forzenbook.domain.usecase.login.CreateAccountRequestUseCase
import com.example.forzenbook.domain.usecase.login.ForgotPasswordResetUseCase
import com.example.forzenbook.domain.usecase.login.LoginGetTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenUseCase: LoginGetTokenUseCase,
    private val forgotPasswordResetUseCase: ForgotPasswordResetUseCase,
    private val createAccountRequestUseCase: CreateAccountRequestUseCase
) : ViewModel() {

    sealed interface LoginState {
        object Error : LoginState

        data class Loading(
            val email: String = "",
            val password: String = "",
            val emailError: Boolean = true,
            val passwordError: Boolean = true
        ) : LoginState

        data class UserInputting(
            val email: String = "",
            val password: String = "",
            val emailError: Boolean = true,
            val passwordError: Boolean = true
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
            val password: String = "",
            val location: String = "",
            val firstError: Boolean = true,
            val lastError: Boolean = true,
            val birthError: Boolean = true,
            val emailError: Boolean = true,
            val passwordError: Boolean = true,
            val locationError: Boolean = true,
        ) : CreateAccountState
    }

    sealed interface ForgotPasswordState {
        object Error : ForgotPasswordState
        object Success : ForgotPasswordState

        data class Loading(
            val email: String = "",
            val emailError: Boolean = true,
        ) : ForgotPasswordState

        data class UserInputting(
            val email: String = "",
            val emailError: Boolean = true,
        ) : ForgotPasswordState
    }

    private var _forgotPasswordState: MutableState<ForgotPasswordState> =
        mutableStateOf(ForgotPasswordState.UserInputting())
    val forgotPasswordState: MutableState<ForgotPasswordState>
        get() = _forgotPasswordState

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
        password: String,
        emailError: Boolean,
        passwordError: Boolean
    ) {
        loginState.value = LoginState.UserInputting(email, password, emailError, passwordError)
    }

    fun updateForgotPasswordText(email: String, emailError: Boolean) {
        forgotPasswordState.value = ForgotPasswordState.UserInputting(email, emailError)
    }

    fun updateCreateAccountTextAndErrors(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String,
        firstError: Boolean,
        lastError: Boolean,
        birthError: Boolean,
        emailError: Boolean,
        passwordError: Boolean,
        locationError: Boolean,
    ) {
        createAccountState.value = CreateAccountState.UserInputting(
            firstName, lastName, birthDay, email, password, location,
            firstError, lastError, birthError, emailError, passwordError, locationError
        )
    }

    fun resetCreateAccountState() {
        createAccountState.value = CreateAccountState.UserInputting()
    }

    fun resetLoginState() {
        loginState.value = LoginState.UserInputting()
    }

    fun resetForgotPasswordState() {
        forgotPasswordState.value = ForgotPasswordState.UserInputting()
    }

    fun submitLogin() {
        viewModelScope.launch {
            if (loginState.value is LoginState.UserInputting) {
                (loginState.value as LoginState.UserInputting).let {
                    val email = it.email
                    val password = it.password
                    val emailError = it.emailError
                    val passwordError = it.passwordError
                    loginState.value =
                        LoginState.Loading(email, password, emailError, passwordError)
                    delay(1000)
                    val token: String? = try {
                        getTokenUseCase(email, password)?.token
                    } catch (e: Exception) {
                        null
                    }
                    //we will want to replace this if section potentially
                    if (token == null) {
                        loginState.value = LoginState.Error
                        //we may also want to show user the error
                    } else {
                        loginState.value =
                            LoginState.UserInputting(email, password, emailError, passwordError)
                        Log.v("Hamdan", "We got the token it is: $token")
                    }

                }
            } else {
                //throw an error here
                Log.v("Hamdan", "There was a major issue")
            }
        }
    }

    fun requestReset() {
        viewModelScope.launch {
            if (forgotPasswordState.value is ForgotPasswordState.UserInputting) {
                val email = (forgotPasswordState.value as ForgotPasswordState.UserInputting).email
                val emailError =
                    (forgotPasswordState.value as ForgotPasswordState.UserInputting).emailError
                forgotPasswordState.value = ForgotPasswordState.Loading(email, emailError)
                delay(1000)
                val code = try {
                    forgotPasswordResetUseCase(email)
                } catch (e: Exception) {
                    0
                }
                if (code in 200 until 300) {
                    Log.v("Hamdan", "Succesful reset")
                    //notify user and gray out reset pass till ready again
                    forgotPasswordState.value = ForgotPasswordState.Success
                    delay(3000)
                    forgotPasswordState.value = ForgotPasswordState.UserInputting(email, emailError)
                    //ToDo Replace this when above is implemented or is being implemented
                } else if (code < 100) {
                    forgotPasswordState.value = ForgotPasswordState.Error
                } else {
                    forgotPasswordState.value = ForgotPasswordState.Error
                    //ToDo prompt with some statement of what the error is
                }
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
                    val password: String = it.password
                    val location: String = it.location
                    createAccountState.value = CreateAccountState.Loading
                    delay(1000)
                    val code = try {
                        createAccountRequestUseCase(
                            firstName,
                            lastName,
                            birthDay,
                            email,
                            password,
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