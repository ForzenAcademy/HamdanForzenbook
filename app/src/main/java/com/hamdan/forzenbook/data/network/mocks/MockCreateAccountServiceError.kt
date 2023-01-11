package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.CreateAccountService
import retrofit2.Response
import java.sql.Date

class MockCreateAccountServiceError : CreateAccountService {
    override suspend fun createUser(
        email: String,
        birthDate: Date,
        firstName: String,
        lastName: String,
        location: String
    ): Response<Void> {
        throw RuntimeException("There was an issue!")
    }
}
