package com.example.forzenbook.data.network.mocks

import com.example.forzenbook.data.network.LoginResponse
import com.example.forzenbook.data.network.LoginService
import retrofit2.Response

class MockLoginServiceNullToken : LoginService {
    override suspend fun getToken(): Response<LoginResponse> {
        return Response.success(LoginResponse(null))
    }
}