package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.GlobalConstants.EMAIL_LENGTH_LIMIT
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.core.validateEmail

class LoginStringValidationUseCaseImpl : LoginStringValidationUseCase {
    override fun invoke(state: LoginEntrys): LoginEntrys {
        val emailError = if (state.email.text.length > EMAIL_LENGTH_LIMIT) {
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
