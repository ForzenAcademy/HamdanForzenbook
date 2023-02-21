package com.hamdan.forzenbook.createaccount.core.data.network.mocks

import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountService
import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountResponse
import retrofit2.Response
import java.sql.Date

class MockCreateAccountServiceError : CreateAccountService {
    override suspend fun createUser(
        email: String,
        birthDate: Date,
        firstName: String,
        lastName: String,
        location: String
    ): Response<CreateAccountResponse> {
        throw RuntimeException("There was an issue!")
    }
}
