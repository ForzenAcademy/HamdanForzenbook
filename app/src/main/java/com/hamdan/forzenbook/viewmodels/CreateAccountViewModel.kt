package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.createaccount.core.viewmodel.getContent
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val createAccountValidationUseCase: CreateAccountValidationUseCase,
    private val createAccountUseCase: CreateAccountUseCase,
) : BaseCreateAccountViewModel(
    createAccountValidationUseCase,
    createAccountUseCase,
) {
    private val _state: MutableState<CreateAccountState> =
        mutableStateOf(CreateAccountState.Content(CreateAccountContent()))

    val state: MutableState<CreateAccountState>
        get() = _state

    override var createAccountState: CreateAccountState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun createAccountClicked() {
        createAccount()
    }

    private fun createAccount() {
        viewModelScope.launch {
            createAccountState.getContent().createAccountContent.let {
                createAccountState = CreateAccountState.Loading
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
                        createAccountState = CreateAccountState.AccountCreated
                        // send to login page
                    }
                    CreateAccountResult.CREATE_EXISTS -> {
                        createAccountState =
                            CreateAccountState.Error(R.string.create_account_error_user_exists)
                    }
                    CreateAccountResult.CREATE_FAILURE -> {
                        createAccountState =
                            CreateAccountState.Error(R.string.create_account_error_generic)
                    }
                }
            }
        }
    }
}
