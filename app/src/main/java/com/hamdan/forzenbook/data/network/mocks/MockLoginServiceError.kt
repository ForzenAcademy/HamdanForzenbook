package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.CreateAccountService
import com.hamdan.forzenbook.data.network.LoginResponse
import com.hamdan.forzenbook.data.network.LoginService
import retrofit2.Response

class MockLoginServiceError:LoginService {
    override suspend fun getToken(): Response<LoginResponse> {
        throw RuntimeException("There was an issue!")
    }
}