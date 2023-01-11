package com.hamdan.forzenbook.data.repository

import com.hamdan.forzenbook.data.network.LoginResponse

interface LoginRepository {
    suspend fun requestValidation(email: String)

    suspend fun getToken(email: String, code: String): LoginResponse?
}
