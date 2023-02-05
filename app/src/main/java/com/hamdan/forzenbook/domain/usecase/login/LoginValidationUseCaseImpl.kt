package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository

class LoginValidationUseCaseImpl(val repository: LoginRepository) :
    LoginValidationUseCase {
    override suspend fun invoke(email: String) {
        repository.requestValidation(email)
    }
}
