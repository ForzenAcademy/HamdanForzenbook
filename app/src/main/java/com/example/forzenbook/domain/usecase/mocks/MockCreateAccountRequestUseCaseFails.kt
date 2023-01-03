package com.example.forzenbook.domain.usecase.mocks

import com.example.forzenbook.domain.usecase.login.CreateAccountRequestUseCase

class MockCreateAccountRequestUseCaseFails : CreateAccountRequestUseCase {
    override suspend fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String
    ): Int {
        throw RuntimeException("Something went wrong requesting Account creation")
    }
}