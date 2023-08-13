package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Todo refactor with new state management practice
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
        MutableStateFlow(CreateAccountState.Content(CreateAccountData()))
    val state: StateFlow<CreateAccountState>
        get() = _state

    override var createAccountState: CreateAccountState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun backIconPressed(context: Context) {
        navigateToLogin(context)
    }

    fun createAccountButtonClicked(context: Context) {
        createAccount(context)
    }

    private fun createAccount(context: Context) {
        viewModelScope.launch {
            createAccountState.getContent().createAccountData.let {
                createAccountState = CreateAccountState.Loading()
                val split = it.birthDay.text.split("-")
                // convert date back to a readable format for the sql on the server
                val actualDate = "${split[2]}-${split[0]}-${split[1]}"
                val result = createAccountUseCase(
                    firstName = it.firstName.text,
                    lastName = it.lastName.text,
                    birthDay = actualDate,
                    email = it.email.text,
                    location = it.location.text,
                )
                when (result) {
                    CreateAccountResult.CREATE_SUCCESS -> {
                        createAccountState = CreateAccountState.AccountCreated()
                        navigator.navigateToLogin(context)
                        createAccountState = CreateAccountState.Content(CreateAccountData())
                        // send to login page
                    }
                    CreateAccountResult.CREATE_EXISTS -> {
                        createAccountState = CreateAccountState.Error(R.string.create_account_error_user_exists)
                    }
                    CreateAccountResult.CREATE_FAILURE -> {
                        createAccountState = CreateAccountState.Error(R.string.create_account_error_generic)
                    }
                }
            }
        }
    }

    private fun navigateToLogin(context: Context) {
        navigator.navigateToLogin(context)
    }
}
