package com.example.forzenbook.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginService {
    @POST("api/login")
    suspend fun getToken(): Response<LoginResponse>
    //apparently we will want to use @Body (probably for hte parameters)
}