package com.example.forzenbook.data.network.mocks

import com.example.forzenbook.data.network.CreateAccountService
import com.example.forzenbook.data.network.LoginResponse
import com.example.forzenbook.data.network.LoginService
import retrofit2.Response

class MockLoginServiceError:LoginService {
    override suspend fun getToken(): Response<LoginResponse> {
        throw RuntimeException("There was an issue!")
    }
}