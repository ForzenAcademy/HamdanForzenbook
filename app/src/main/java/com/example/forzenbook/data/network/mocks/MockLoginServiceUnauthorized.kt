package com.example.forzenbook.data.network.mocks

import com.example.forzenbook.data.network.LoginResponse
import com.example.forzenbook.data.network.LoginService
import okhttp3.ResponseBody
import retrofit2.Response

class MockLoginServiceUnauthorized : LoginService {
    override suspend fun getToken(): Response<LoginResponse> {
        return Response.error(401, ResponseBody.create(null, "error content"))
    }
}