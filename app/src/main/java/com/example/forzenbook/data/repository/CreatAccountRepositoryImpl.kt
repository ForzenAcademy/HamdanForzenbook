package com.example.forzenbook.data.repository

import com.example.forzenbook.data.network.CreateAccountService

class CreateAccountRepositoryImpl(
    private val service: CreateAccountService
) : CreateAccountRepository {

    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String
    ): Int {
        return service.createUser(firstName, lastName, birthDay, email, password, location).code()
    }
}