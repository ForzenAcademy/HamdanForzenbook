package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository

class LoginRequestValidationUseCaseImpl(val repository: LoginRepository) :
    LoginRequestValidationUseCase {
    override suspend fun invoke(email: String) {
        repository.requestValidation(email)
    }
}
