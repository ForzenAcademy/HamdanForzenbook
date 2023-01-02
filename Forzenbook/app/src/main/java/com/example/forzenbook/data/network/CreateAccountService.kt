package com.example.forzenbook.data.network

import retrofit2.Response
import retrofit2.http.POST

interface CreateAccountService {
    @POST("/createUser")    //ToDo(need to add the email to this path, ask nic what the post should look like for this)
    suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String
    ): Response<Any>
    //apparently we will want to use @Body (probably for hte parameters)
}