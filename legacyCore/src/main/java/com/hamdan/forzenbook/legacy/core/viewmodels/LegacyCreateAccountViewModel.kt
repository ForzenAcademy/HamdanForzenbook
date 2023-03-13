package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.legacy.core.view.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacyCreateAccountViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
    private val navigator: Navigator
) : BaseCreateAccountViewModel(
    createAccountValidationUseCase,
    createAccountUseCase
) {
    private val _state: MutableStateFlow<CreateAccountState> =
        MutableStateFlow(CreateAccountState())
    val state: StateFlow<CreateAccountState>
        get() = _state

    override var createAccountState: CreateAccountState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun onAccountCreateFinish(context: Context) {
        navigateToLogin(context)
    }

    fun backIconPressed(context: Context) {
        navigateToLogin(context)
    }

    private fun navigateToLogin(context: Context) {
        navigator.navigateToLogin(context)
    }
}
