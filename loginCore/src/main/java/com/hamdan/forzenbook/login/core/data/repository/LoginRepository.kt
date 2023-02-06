package com.hamdan.forzenbook.login.core.data.repository

interface LoginRepository {
    suspend fun requestValidation(email: String)

    suspend fun getToken(email: String? = null, code: String? = null): User
}
