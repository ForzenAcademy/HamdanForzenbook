package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacyLoginViewModel @Inject constructor(
    private val getTokenFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val getTokenFromDatabaseUseCase: LoginGetStoredCredentialsUseCase,
    private val requestValidationCode: LoginValidationUseCase,
    private val navigator: Navigator,
) : BaseLoginViewModel(
    getTokenFromNetworkUseCase,
    getTokenFromDatabaseUseCase,
    requestValidationCode
) {
    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Content(LoginContent.Email()))
    val state: StateFlow<LoginState>
        get() = _state

    override var loginState: LoginState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun login(context: Context) {
        // Todo use navigator to navigate to main page FA-Jamie please make ticket big sadge
    }

    fun createAccountLinkPressed(context: Context) {
        navigateToCreateAccount(context)
    }

    private fun navigateToCreateAccount(context: Context) {
        navigator.navigateToCreateAccount(context)
    }
}
