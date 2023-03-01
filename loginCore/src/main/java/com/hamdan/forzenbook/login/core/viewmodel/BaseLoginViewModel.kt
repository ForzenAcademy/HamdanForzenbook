package com.hamdan.forzenbook.login.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.core.stringForm
import com.hamdan.forzenbook.login.core.domain.usecase.LoginEntrys
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginStringValidationUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import com.hamdan.forzenbook.login.core.view.LoginUiState
import kotlinx.coroutines.launch

abstract class BaseLoginViewModel(
    private val loginStringValidationUseCase: LoginStringValidationUseCase,
    private val getTokenFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val getTokenFromDatabaseUseCase: LoginGetStoredCredentialsUseCase,
    private val requestValidationCode: LoginValidationUseCase,
) : ViewModel() {
    data class LoginState(
        val email: Entry = Entry("", LoginError.EmailError.None),
        val code: Entry = Entry("", LoginError.CodeError.None),
        val showInfoDialog: Boolean = false,
        val inputtingCode: Boolean = false,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
    )

    protected abstract var loginState: LoginState

    fun updateLoginTexts(
        email: Entry,
        code: Entry,
        isInputtingCode: Boolean,
    ) {
        val stringStates = loginStringValidationUseCase(
            loginState.copy(
                email = email,
                code = code,
                inputtingCode = isInputtingCode,
            ).toLoginEntrys()
        )
        loginState =
            loginState.copy(email = stringStates.email, code = stringStates.code)
    }

    private fun loginNormalView() {
        loginState =
            loginState.copy(hasError = false, showInfoDialog = false, isLoading = false)
    }

    fun loginDismissErrorClicked() {
        loginNormalView()
    }

    fun loginDismissInfoClicked() {
        loginNormalView()
    }

    private fun submitLogin() {
        viewModelScope.launch {
            loginState.let {
                val email = it.email
                val code = it.code
                loginState =
                    loginState.copy(
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
                    loginState = loginState.copy(
                        email = Entry(text = "", error = LoginError.EmailError.Valid),
                        code = Entry(text = "", error = LoginError.CodeError.Length),
                        showInfoDialog = false,
                        inputtingCode = false,
                        isLoading = false,
                        hasError = false
                    )
                    // for now we will show a reseted page
                } catch (e: Exception) {
                    loginState = loginState.copy(
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
            loginState.let {
                loginState = loginState.copy(
                    isLoading = true,
                )
                loginState = try {
                    requestValidationCode(it.email.text)
                    loginState.copy(
                        inputtingCode = true,
                        showInfoDialog = true,
                        isLoading = false,
                    )
                } catch (e: Exception) {
                    loginState.copy(
                        inputtingCode = false,
                        isLoading = false,
                        hasError = true,
                    )
                }
            }
        }
    }

    fun loginClicked() {
        if (loginState.inputtingCode) {
            submitLogin()
        } else {
            requestLoginValidationCode()
        }
    }
}

fun BaseLoginViewModel.LoginState.toLoginEntrys(): LoginEntrys =
    LoginEntrys(
        email = this.email,
        code = this.code,
    )

fun BaseLoginViewModel.LoginState.toLoginUiState(): LoginUiState =
    LoginUiState(
        email = this.email,
        code = this.code,
        showInfoDialog = this.showInfoDialog,
        inputtingCode = this.inputtingCode,
        isLoading = this.isLoading,
        hasError = this.hasError
    )

fun BaseLoginViewModel.LoginState.stringForm(): String {
    val code: LoginError = this.code.error as LoginError
    val email: LoginError = this.email.error as LoginError
    return "text: ${this.email.text}, ${email.stringForm()} \n text: ${this.code.text}, ${code.stringForm()} " +
        "\n inputting: ${this.inputtingCode} " +
        "\n hasError: ${this.hasError}" +
        "\n loading: ${this.isLoading}" +
        "\n showdialog :${this.showInfoDialog}"
}
