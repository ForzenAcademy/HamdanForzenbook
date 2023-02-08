package com.hamdan.forzenbook.createaccount.core.domain.mocks

import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase

class MockCreateAccountUseCaseFails : CreateAccountUseCase {
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
