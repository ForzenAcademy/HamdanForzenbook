package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository

class LoginUseCaseValidationImpl(val repository: LoginRepository) :
    LoginUseCaseValidation {
    override suspend fun invoke(email: String) {
        repository.requestValidation(email)
    }
}
