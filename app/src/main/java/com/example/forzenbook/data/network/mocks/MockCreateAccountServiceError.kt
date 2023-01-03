package com.example.forzenbook.data.network.mocks

import com.example.forzenbook.data.network.CreateAccountService
import retrofit2.Response

class MockCreateAccountServiceError:CreateAccountService {
    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String
    ): Response<Any> {
        throw RuntimeException("There was an issue!")
    }
}