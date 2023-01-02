package com.example.forzenbook.domain.usecase.mocks

import com.example.forzenbook.data.repository.LoginData
import com.example.forzenbook.data.repository.LoginRepository
import com.example.forzenbook.domain.usecase.login.LoginGetTokenUseCase

class MockLoginGetTokenUseCaseSuccess(
    val repository: LoginRepository
) : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, password: String): LoginData? {
        return LoginData("it worked")
    }
}