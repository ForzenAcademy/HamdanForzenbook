package com.hamdan.forzenbook.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCase
import com.hamdan.forzenbook.createaccount.core.viewmodel.BaseCreateAccountViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
        mutableStateOf(
            CreateAccountState(
                errorId = null,
                firstName = Entry(text = "", error = LoginError.NameError.None),
                lastName = Entry(text = "", error = LoginError.NameError.None),
                birthDay = Entry(text = "", error = LoginError.BirthDateError.None),
                email = Entry(text = "", error = LoginError.EmailError.None),
                location = Entry(text = "", error = LoginError.LocationError.None),
                isDateDialogOpen = false,
                isLoading = false
            )
        )

    val state: MutableState<CreateAccountState>
        get() = _state

    override var createAccountState: CreateAccountState
        get() = _state.value
        set(value) {
            _state.value = value
        }
}
