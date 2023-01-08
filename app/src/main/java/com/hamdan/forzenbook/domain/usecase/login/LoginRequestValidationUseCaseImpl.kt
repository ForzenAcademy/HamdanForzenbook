package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository

class LoginRequestValidationUseCaseImpl(val repository: LoginRepository) :
    LoginRequestValidationUseCase {
    override suspend fun invoke(email: String): Boolean {
        return try {
            repository.requestValidation(email)
        } catch (e: Exception) {
            false
        }
    }
}
