package com.hamdan.forzenbook.data.repository

import android.util.Log
import com.hamdan.forzenbook.data.network.CreateAccountService

class CreateAccountRepositoryImpl(
    private val service: CreateAccountService
) : CreateAccountRepository {

    override suspend fun createUser(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): Int {
        return try {
            service.createUser(firstName, lastName, birthDay, email, location)
                .code()
        } catch (e: Exception) {
            Log.v("Hamdan", "There was an unidentifiable issue when creating the account")
            0
        }
    }
}
