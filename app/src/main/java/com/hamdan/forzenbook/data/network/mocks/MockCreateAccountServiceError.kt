package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.CreateAccountService
import retrofit2.Response

class MockCreateAccountServiceError:CreateAccountService {
    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): Response<Any> {
        throw RuntimeException("There was an issue!")
    }
}