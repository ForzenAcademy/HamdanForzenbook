package com.hamdan.forzenbook.applegacy.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.login.core.domain.usecase.LoginEntrys
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginStringValidationUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LegacyLoginViewModel @Inject constructor(
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

    // TODO REMOVE THIS FUNCTION WHEN WORKING ON Legacy Login FA-100
    fun test() {
        Log.v("Hamdan", "Hello")
    }

    private var email: Entry = Entry("", LoginError.EmailError.None)
    private var code: Entry = Entry("", LoginError.CodeError.None)
    private var showInfoDialog: Boolean = false
    private var inputtingCode: Boolean = false
    private var isLoading: Boolean = false
    private var hasError: Boolean = false

    var onLoginUpdate: ((LoginState) -> Unit)? = null

    private fun updateLoginState() {
        onLoginUpdate?.invoke(
            LoginState(
                email = email,
                code = code,
                showInfoDialog = showInfoDialog,
                inputtingCode = inputtingCode,
                isLoading = isLoading,
                hasError = hasError
            )
        )
    }

    fun updateLoginTexts(
        inEmail: String,
        inCode: String,
    ) {
        email = email.copy(text = inEmail)
        code = code.copy(text = inCode)
        val stringStates = loginStringValidationUseCase(LoginEntrys(email, code))
        email = stringStates.email
        code = stringStates.code
        updateLoginState()
    }

    private fun loginNormalView() {
        hasError = false
        showInfoDialog = false
        isLoading = false
    }

    fun loginDismissErrorClicked() {
        loginNormalView()
        updateLoginState()
    }

    fun loginDismissInfoClicked() {
        loginNormalView()
        updateLoginState()
    }

    private fun submitLogin() {
        viewModelScope.launch {
            val tempEmail = email
            val tempCode = code
            inputtingCode = true
            isLoading = true
            hasError = false
            updateLoginState()
            try {
                getTokenFromNetworkUseCase(tempEmail.text, tempCode.text)
                // TODO here they would be considered succesful
                // send off to where the user needs to see next, that location will retrieve token from DB
                email = Entry(text = "", error = LoginError.EmailError.Valid)
                code = Entry(text = "", error = LoginError.CodeError.Length)
                showInfoDialog = false
                inputtingCode = false
                isLoading = false
                hasError = false
                // for now we will show a reseted page
            } catch (e: Exception) {
                email = tempEmail
                code = tempCode
                inputtingCode = true
                isLoading = false
                hasError = true
            }
            updateLoginState()
        }
    }

    private fun requestLoginValidationCode() {
        viewModelScope.launch {
            isLoading = true
            updateLoginState()
            try {
                requestValidationCode(email.text)
                inputtingCode = true
                showInfoDialog = true
                isLoading = false
            } catch (e: Exception) {
                inputtingCode = true
                isLoading = false
                hasError = true
            }
            updateLoginState()
        }
    }

    fun loginClicked() {
        if (inputtingCode) {
            submitLogin()
        } else {
            requestLoginValidationCode()
        }
    }
}
