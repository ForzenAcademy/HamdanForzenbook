package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.LoginResponse
import com.hamdan.forzenbook.data.network.LoginService
import retrofit2.Response

class MockLoginServiceNullToken : LoginService {
    override suspend fun getToken(): Response<LoginResponse> {
        return Response.success(LoginResponse(null))
    }
}