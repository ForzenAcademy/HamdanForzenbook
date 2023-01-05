package com.hamdan.forzenbook.data.repository

interface LoginRepository {

    suspend fun getToken(email: String, password: String): LoginData?

}