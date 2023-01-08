package com.hamdan.forzenbook.data.repository.mocks

import com.hamdan.forzenbook.data.network.CreateAccountService
import com.hamdan.forzenbook.data.repository.CreateAccountRepository

class MockCreateAccountRepositoryImplSucceeds(
    createAccountService: CreateAccountService
) : CreateAccountRepository {

    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): Int {
        // TODO remove response code FA-80
        return 200
    }
}
