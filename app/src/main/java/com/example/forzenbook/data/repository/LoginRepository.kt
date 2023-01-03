package com.example.forzenbook.data.repository

interface LoginRepository {

    suspend fun getToken(email: String, password: String): LoginData?

}