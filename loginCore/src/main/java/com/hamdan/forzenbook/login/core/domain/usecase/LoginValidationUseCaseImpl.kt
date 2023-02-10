package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.login.core.data.repository.LoginRepository

class LoginValidationUseCaseImpl(val repository: LoginRepository) :
    LoginValidationUseCase {
    override suspend fun invoke(email: String) {
        repository.requestValidation(email)
    }
}
