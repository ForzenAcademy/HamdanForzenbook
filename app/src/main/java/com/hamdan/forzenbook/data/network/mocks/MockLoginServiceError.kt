package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.LoginResponse
import com.hamdan.forzenbook.data.network.LoginService
import retrofit2.Response

class MockLoginServiceError : LoginService {
    override suspend fun requestValidation(email: String): Response<Void> {
        throw RuntimeException("There was an issue!")
    }
    override suspend fun getToken(email: String, code: String): Response<LoginResponse> {
        throw RuntimeException("There was an issue!")
    }
}
