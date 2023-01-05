package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.LoginResponse
import com.hamdan.forzenbook.data.network.LoginService
import okhttp3.ResponseBody
import retrofit2.Response

class MockLoginServiceUnauthorized : LoginService {
    override suspend fun getToken(): Response<LoginResponse> {
        return Response.error(401, ResponseBody.create(null, "error content"))
    }
}