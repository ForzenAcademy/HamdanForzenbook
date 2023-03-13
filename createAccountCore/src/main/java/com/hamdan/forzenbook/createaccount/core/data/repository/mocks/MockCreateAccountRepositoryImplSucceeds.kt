package com.hamdan.forzenbook.createaccount.core.data.repository.mocks

import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountService
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepository

class MockCreateAccountRepositoryImplSucceeds(
    createAccountService: CreateAccountService
) : CreateAccountRepository {
    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ) {}
}
