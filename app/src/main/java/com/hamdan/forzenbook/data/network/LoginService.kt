package com.hamdan.forzenbook.data.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @GET(LOGIN_GET)
    suspend fun requestValidation(
        @Query(EMAIL) email: String
    ): Response<Void>

    @FormUrlEncoded
    @POST(LOGIN_POST)
    suspend fun getToken(
        @Field(EMAIL) email: String,
        @Field(CODE) code: String,
    ): Response<LoginResponse>

    companion object {
        private const val EMAIL = "email"
        private const val CODE = "code"
        private const val LOGIN_GET = "login"
        private const val LOGIN_POST = "login/"
    }
}
