package com.hamdan.forzenbook.createaccount.core.data.repository

interface CreateAccountRepository {
    suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String,
    )
}
