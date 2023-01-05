package com.hamdan.forzenbook.data.network

import retrofit2.Response
import retrofit2.http.POST

interface ForgotPasswordService {
    //not the correct endpoint as well
    @POST("/createUser")    //ToDo(need to add the email to this path, ask nic what the post should look like for this)
    suspend fun requestReset(): Response<Any>
    //apparently we will want to use @Body (probably for hte parameters)
}