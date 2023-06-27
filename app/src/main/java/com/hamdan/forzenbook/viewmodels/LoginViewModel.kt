package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getTokenFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val getTokenFromDatabaseUseCase: LoginGetStoredCredentialsUseCase,
    private val requestValidationCode: LoginValidationUseCase,
) : BaseLoginViewModel(
    getTokenFromNetworkUseCase,
    getTokenFromDatabaseUseCase,
    requestValidationCode
) {
    private val _state: MutableState<LoginState> =
        mutableStateOf(LoginState.Content(LoginContent.Email()))
    val state: MutableState<LoginState>
        get() = _state

    override var loginState: LoginState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
