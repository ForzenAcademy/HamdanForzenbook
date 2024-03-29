package com.hamdan.forzenbook.legacy.core.viewmodels

import androidx.fragment.app.FragmentManager
import com.hamdan.forzenbook.legacy.core.view.FragmentNavigator
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetCredentialsFromNetworkUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCase
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCase
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacyLoginFragmentViewModel @Inject constructor(
    private val getTokenFromNetworkUseCase: LoginGetCredentialsFromNetworkUseCase,
    private val getTokenFromDatabaseUseCase: LoginGetStoredCredentialsUseCase,
    private val requestValidationCode: LoginValidationUseCase,
    private val navigator: FragmentNavigator,
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

    fun login() {
        // Todo use navigator to navigate to main page when implemented
        // only bother with this if a client actualy needs it to be in Fragments, for now
        // Java and Fragments will not be developed further
    }

    fun createAccountLinkPressed(fragmentManager: FragmentManager) {
        navigateToCreateAccount(fragmentManager)
    }

    private fun navigateToCreateAccount(fragmentManager: FragmentManager) {
        navigator.navigateToCreateAccount(fragmentManager)
    }
}
