package com.example.forzenbook.data.repository

import android.util.Log
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
        return try {
            service.createUser(firstName, lastName, birthDay, email, password, location)
                .code()
        } catch (e: Exception) {
            Log.v("Hamdan", "There was an unidentifiable issue when creating the account")
            0
        }
    }
}