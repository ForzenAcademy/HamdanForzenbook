package com.hamdan.forzenbook.applegacy.viewmodels

import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LegacyCreateAccountViewModel @Inject constructor(
    private val createAccountUseCase: CreateAccountUseCase,
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
) : BaseCreateAccountViewModel(createAccountValidationUseCase, createAccountUseCase) {

    private val _state = MutableStateFlow(createAccountState)
    val state: StateFlow<CreateAccountState>
        get() = _state

    override var createAccountState: CreateAccountState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
