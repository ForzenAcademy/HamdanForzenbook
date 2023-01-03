package com.example.forzenbook.domain.usecase.login

import com.example.forzenbook.data.repository.LoginData
import com.example.forzenbook.data.repository.LoginRepository

class LoginGetTokenUseCaseImpl(
    val repository: LoginRepository
) : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, password: String): LoginData? {
        return repository.getToken(email, password)
    }
}