package com.hamdan.forzenbook.legacy.core.viewmodels

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.ui.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    fun backIconPressed(context: Context) {
        navigateToLogin(context)
    }

    fun createAccountButtonClicked(context: Context) {
        createAccount(context)
    }

    private fun createAccount(context: Context) {
        viewModelScope.launch {
            createAccountState.let {
                createAccountState = createAccountState.copy(
                    isLoading = true,
                )
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
                        createAccountState = createAccountState.copy(
                            firstName = Entry(text = "", error = LoginError.NameError.Length),
                            lastName = Entry(text = "", error = LoginError.NameError.Length),
                            birthDay = Entry(text = "", error = LoginError.BirthDateError.TooYoung),
                            email = Entry(text = "", error = LoginError.EmailError.Valid),
                            location = Entry(text = "", error = LoginError.LocationError.Length),
                            isLoading = false,
                            accountCreated = true,
                        )
                        // send to login page
                        navigator.navigateToLogin(context)
                    }
                    CreateAccountResult.CREATE_EXISTS -> {
                        createAccountState = createAccountState.copy(
                            errorId = R.string.create_account_error_user_exists,
                            isLoading = false,
                        )
                    }
                    CreateAccountResult.CREATE_FAILURE -> {
                        createAccountState = createAccountState.copy(
                            errorId = R.string.create_account_error_generic,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    private fun navigateToLogin(context: Context) {
        navigator.navigateToLogin(context)
    }
}
