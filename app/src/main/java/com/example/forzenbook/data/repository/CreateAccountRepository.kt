package com.example.forzenbook.data.repository

interface CreateAccountRepository {

    suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String,
    ): Int

}