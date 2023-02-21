package com.hamdan.forzenbook.createaccount.core.data.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.sql.Date

interface CreateAccountService {
    @FormUrlEncoded
    @POST(CREATE_USER)
    suspend fun createUser(
        @Field(EMAIL) email: String,
        @Field(BIRTH_DATE) birthDate: Date,
        @Field(FIRST_NAME) firstName: String,
        @Field(LAST_NAME) lastName: String,
        @Field(LOCATION) location: String
    ): Response<CreateAccountResponse>
    companion object {
        private const val EMAIL = "email"
        private const val BIRTH_DATE = "birth_date"
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val LOCATION = "location"
        private const val CREATE_USER = "user/"
    }
}
