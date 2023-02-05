package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.view.composables.validateEmail
import com.hamdan.forzenbook.view.login.LoginError
import com.hamdan.forzenbook.view.login.LoginSharedConstants
import com.hamdan.forzenbook.viewmodels.Entry
import com.hamdan.forzenbook.viewmodels.LoginViewModel

class LoginStringValidationUseCaseImpl : LoginStringValidationUseCase {
    override fun invoke(state: LoginViewModel.LoginState): LoginViewModel.LoginState {
        val emailError = if (state.email.text.length > LoginSharedConstants.EMAIL_LENGTH_LIMIT) {
            LoginError.EmailError.Length
        } else if (!validateEmail(state.email.text)) {
            LoginError.EmailError.InvalidFormat
        } else LoginError.EmailError.Valid
        val codeError = if (state.code.text.length > CODE_LENGTH_MAX) {
            LoginError.CodeError.Length
        } else LoginError.CodeError.Valid
        return state.copy(
            email = Entry(text = state.email.text, error = emailError),
            code = Entry(text = state.code.text, error = codeError),
        )
    }

    companion object {
        private const val CODE_LENGTH_MAX = 6
    }
}
