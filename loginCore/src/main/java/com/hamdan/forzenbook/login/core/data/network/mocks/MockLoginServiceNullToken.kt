package com.hamdan.forzenbook.login.core.data.network.mocks

import com.hamdan.forzenbook.login.core.data.network.LoginResponse
import com.hamdan.forzenbook.login.core.data.network.LoginService
import retrofit2.Response

class MockLoginServiceNullToken : LoginService {
    override suspend fun requestValidation(email: String): Response<Void> {
        return Response.success(null)
    }
    override suspend fun getToken(email: String, code: String): Response<LoginResponse> {
        return Response.success(LoginResponse(null))
    }
}
