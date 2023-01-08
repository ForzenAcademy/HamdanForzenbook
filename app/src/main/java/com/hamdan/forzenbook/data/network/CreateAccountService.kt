package com.hamdan.forzenbook.data.network

import com.hamdan.forzenbook.data.network.NetworkResources.CREATE_USER
import retrofit2.Response
import retrofit2.http.POST

interface CreateAccountService {
    @POST(CREATE_USER) // ToDo(need to add the email to this path, ask nic what the post should look like for this)
    suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): Response<Any>
    // apparently we will want to use @Body (probably for hte parameters)
}
