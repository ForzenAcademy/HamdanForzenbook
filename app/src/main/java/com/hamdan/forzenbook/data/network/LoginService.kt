package com.hamdan.forzenbook.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginService {

    @GET(NetworkResources.LOGIN)
    suspend fun requestValidation(
        @Query(NetworkResources.EMAIL) email: String
    ): Response<Void>

    // TODO Implement Getting the Token
    // @POST
}
