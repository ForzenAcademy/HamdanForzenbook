package com.hamdan.forzenbook.data.repository

import com.hamdan.forzenbook.data.network.CreateAccountService
import java.sql.Date

class CreateAccountRepositoryImpl(
    private val service: CreateAccountService
) : CreateAccountRepository {
    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ) {
        val response = service.createUser(
            email, Date.valueOf(birthDay), firstName, lastName, location
        )
        if (!response.isSuccessful) {
            if (response.code() == USER_EXISTS) throw(AccountException("User Already Exists"))
            else throw(Exception("Unknown Error"))
        }
    }

    companion object {
        private const val USER_EXISTS = 409
    }
}

class AccountException(message: String) : Exception(message)
