package com.hamdan.forzenbook.createaccount.core.data.repository

import android.util.Log
import com.google.gson.Gson
import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountResponse
import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountService
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
            if (response.code() == INPUT_ERROR) {
                val msg = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    CreateAccountResponse::class.java
                )
                Log.e("CreateAccount", "${msg.reason}")
                throw InputException()
            } else throw(Exception("Unknown Error"))
        }
    }

    companion object {
        private const val USER_EXISTS = 409
        private const val INPUT_ERROR = 400
    }
}

class AccountException(message: String) : Exception(message)
class InputException() : Exception()
