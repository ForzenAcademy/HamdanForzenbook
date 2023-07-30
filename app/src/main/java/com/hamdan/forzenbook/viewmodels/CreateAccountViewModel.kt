package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.StateException
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
        mutableStateOf(CreateAccountState.Content(CreateAccountData()))

    val state: MutableState<CreateAccountState>
        get() = _state

    override var createAccountState: CreateAccountState
        get() = _state.value
        set(value) {
            _state.value = value
        }

    fun createAccountClicked() {
        val currentState = createAccountState
        if (currentState is CreateAccountState.Content) {
            viewModelScope.launch {
                createAccountState.getContent().createAccountData.let {
                    createAccountState = CreateAccountState.Loading(currentState.createAccountData)
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
                            createAccountState = CreateAccountState.AccountCreated(null)
                        }

                        CreateAccountResult.CREATE_EXISTS -> {
                            createAccountState =
                                CreateAccountState.Error(
                                    R.string.create_account_error_user_exists,
                                    currentState.createAccountData
                                )
                        }

                        CreateAccountResult.CREATE_FAILURE -> {
                            createAccountState =
                                CreateAccountState.Error(
                                    R.string.create_account_error_generic,
                                    currentState.createAccountData
                                )
                        }
                    }
                }
            }
        } else throw StateException()
    }
}
