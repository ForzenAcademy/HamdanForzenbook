package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.domain.usecase.login.CreateAccountRequestUseCase
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountResult

class MockCreateAccountRequestUseCaseFails : CreateAccountRequestUseCase {
    override suspend fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): CreateAccountResult {
        throw RuntimeException("Something went wrong requesting Account creation")
    }
}
