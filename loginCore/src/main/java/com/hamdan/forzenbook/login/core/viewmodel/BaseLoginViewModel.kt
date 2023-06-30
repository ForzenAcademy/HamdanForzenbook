package com.hamdan.forzenbook.login.core.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.GlobalConstants.TOKEN_KEY
import com.hamdan.forzenbook.core.GlobalConstants.TOKEN_PREFERENCE_LOCATION
import com.hamdan.forzenbook.core.validateEmail
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import kotlinx.coroutines.launch

abstract class BaseLoginViewModel(
    private val loginGetCredentialsFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val loginGetStoredCredentialsUseCase: LoginGetStoredCredentialsUseCase,
    private val loginValidationUseCase: LoginValidationUseCase,
) : ViewModel() {

    sealed interface LoginContent {
        data class Email(val email: Entry = Entry("", EntryError.EmailError.None)) : LoginContent
        data class Code(
            val email: String,
            val code: Entry = Entry("", EntryError.CodeError.None),
            val showInfoDialog: Boolean = false,
        ) : LoginContent
    }

    enum class LoginInputType {
        EMAIL, CODE,
    }

    sealed interface LoginState {
        data class Content(val content: LoginContent) : LoginState
        data class Loading(
            val loginInputType: LoginInputType,
            val email: String,
        ) : LoginState

        data class Error(
            val loginInputType: LoginInputType,
            val email: String,
        ) : LoginState

        object LoggedIn : LoginState
    }

    protected abstract var loginState: LoginState

    fun checkLoggedIn() {
        viewModelScope.launch {
            try {
                loginGetStoredCredentialsUseCase()
                loginState = LoginState.LoggedIn
            } catch (e: Exception) {
                LoginState.Content(LoginContent.Email())
            }
        }
    }

    fun updateText(
        entry: Entry,
    ) {
        if (loginState.getContent() is LoginContent.Email) {
            updateLoginEmail(entry)
        } else {
            updateLoginCode(entry)
        }
    }

    fun kickBackToLogin(context: Context) {
        reset(context)
    }

    private fun reset(context: Context) {
        viewModelScope.launch {
            context.getSharedPreferences(
                TOKEN_PREFERENCE_LOCATION,
                Context.MODE_PRIVATE
            ).edit().apply {
                remove(TOKEN_KEY)
                apply()
            }
            loginState = LoginState.Content(LoginContent.Email())
        }
    }

    private fun updateLoginEmail(
        email: Entry,
    ) {
        val newEmail = Entry(
            email.text,
            if (email.text.length > GlobalConstants.EMAIL_LENGTH_LIMIT) {
                EntryError.EmailError.Length
            } else if (!validateEmail(email.text)) {
                EntryError.EmailError.InvalidFormat
            } else EntryError.EmailError.Valid,
        )
        loginState = LoginState.Content(LoginContent.Email(newEmail))
    }

    private fun updateLoginCode(
        code: Entry,
    ) {
        val newCode = code.copy(
            error = if (code.text.length > CODE_LENGTH_MAX) {
                EntryError.CodeError.Length
            } else EntryError.CodeError.Valid
        )
        loginState =
            LoginState.Content((loginState.getContent() as LoginContent.Code).copy(code = newCode))
    }

    fun loginDismissErrorClicked() {
        loginDismissError()
    }

    private fun loginDismissError() {
        loginState = if ((loginState as LoginState.Error).loginInputType == LoginInputType.EMAIL) {
            LoginState.Content(LoginContent.Email())
        } else {
            LoginState.Content(LoginContent.Code((loginState as LoginState.Error).email))
        }
    }

    fun loginDismissInfoClicked() {
        dismissInfo()
    }

    private fun dismissInfo() {
        loginState =
            LoginState.Content((loginState.getContent() as LoginContent.Code).copy(showInfoDialog = false))
    }

    private fun submitLogin() {
        viewModelScope.launch {
            (loginState.getContent() as LoginContent.Code).let {
                val email = it.email
                val code = it.code.text
                loginState = LoginState.Loading(LoginInputType.CODE, email)
                try {
                    loginGetCredentialsFromNetworkUseCase(email, code)
                    loginState = LoginState.LoggedIn
                } catch (e: Exception) {
                    loginState = LoginState.Error(LoginInputType.CODE, email)
                }
            }
        }
    }

    private fun requestLoginValidationCode() {
        viewModelScope.launch {
            (loginState.getContent() as LoginContent.Email).let {
                loginState = LoginState.Loading(LoginInputType.EMAIL, it.email.text)
                loginState = try {
                    loginValidationUseCase(it.email.text)
                    LoginState.Content(LoginContent.Code(it.email.text, showInfoDialog = true))
                } catch (e: Exception) {
                    LoginState.Error(LoginInputType.EMAIL, it.email.text)
                }
            }
        }
    }

    fun loginClicked() {
        if (loginState.getContent() is LoginContent.Code) {
            submitLogin()
        } else {
            requestLoginValidationCode()
        }
    }

    companion object {
        private const val CODE_LENGTH_MAX = 6
    }
}

// not made safe as if used in the wrong spot the app should crash, it is not intended to act in that way
fun BaseLoginViewModel.LoginState.getContent(): BaseLoginViewModel.LoginContent =
    (this as BaseLoginViewModel.LoginState.Content).content
