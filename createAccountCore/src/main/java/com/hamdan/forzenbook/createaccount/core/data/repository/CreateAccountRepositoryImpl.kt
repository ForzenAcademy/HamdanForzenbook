package com.hamdan.forzenbook.createaccount.core.data.repository

import android.util.Log
import com.google.gson.Gson
import com.hamdan.forzenbook.core.AccountException
import com.hamdan.forzenbook.core.InputException
import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountErrorResponse
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
            if (response.code() == USER_EXISTS) throw (AccountException("User Already Exists"))
            if (response.code() == INPUT_ERROR) {
                val msg = Gson().fromJson(
                    response.errorBody()?.charStream(),
                    CreateAccountErrorResponse::class.java
                )
                Log.e("Exception", "${msg.reason}")
                throw InputException()
            } else throw (Exception("Unknown Error"))
        }
    }

    companion object {
        private const val USER_EXISTS = 409
        private const val INPUT_ERROR = 400
    }
}
