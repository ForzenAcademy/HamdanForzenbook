package com.hamdan.forzenbook.data.repository

interface LoginRepository {

    suspend fun requestValidation(email: String): Boolean

    suspend fun getToken(email: String, code: String): LoginData?
    // TODO Implement Getting the Token

}
