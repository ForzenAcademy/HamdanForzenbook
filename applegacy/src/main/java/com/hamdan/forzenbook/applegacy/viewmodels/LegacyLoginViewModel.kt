package com.hamdan.forzenbook.applegacy.viewmodels

import android.util.Log
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginStringValidationUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacyLoginViewModel @Inject constructor(
    private val loginStringValidationUseCase: LoginStringValidationUseCase,
    private val getTokenFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val getTokenFromDatabaseUseCase: LoginGetStoredCredentialsUseCase,
    private val requestValidationCode: LoginValidationUseCase,
) : BaseLoginViewModel(
    loginStringValidationUseCase,
    getTokenFromNetworkUseCase,
    getTokenFromDatabaseUseCase,
    requestValidationCode
) {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState>
        get() = _state

    override var loginState: LoginState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    // TODO REMOVE THIS FUNCTION WHEN WORKING ON Legacy Login FA-100
    // region removables
    fun test() {
        Log.v("Hamdan", "Hello")
    }

    data class Example(val ex: Int)

    private val _test = MutableStateFlow(Example(0))
    val testState: StateFlow<Example>
        get() = _test

    val temp = 5

    fun testFunc() {
        _test.value = testState.value.copy(ex = (testState.value.ex + temp))
    }
    // endregion
}
