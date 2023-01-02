package com.example.forzenbook.data.repository.mocks

import com.example.forzenbook.data.network.CreateAccountService
import com.example.forzenbook.data.repository.CreateAccountRepository

class MockCreateAccountRepositoryImplSucceeds(
    createAccountService: CreateAccountService
) : CreateAccountRepository {

    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String
    ): Int {
        return 200
    }
}