package com.hamdan.forzenbook.data.repository

interface CreateAccountRepository {

    suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String,
    ): Int

}