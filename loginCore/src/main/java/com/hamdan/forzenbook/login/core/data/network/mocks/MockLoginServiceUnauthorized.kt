package com.hamdan.forzenbook.login.core.data.network.mocks

import com.hamdan.forzenbook.login.core.data.network.LoginResponse
import com.hamdan.forzenbook.login.core.data.network.LoginService
import okhttp3.ResponseBody
import retrofit2.Response

class MockLoginServiceUnauthorized : LoginService {
    override suspend fun requestValidation(email: String): Response<Void> {
        return Response.success(null)
    }
    override suspend fun getToken(email: String, code: String): Response<LoginResponse> {
        return Response.error(401, ResponseBody.create(null, "error content"))
    }
}
