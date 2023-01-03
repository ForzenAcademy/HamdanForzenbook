package com.example.forzenbook.data.repository

import com.example.forzenbook.data.database.LoginDao
import com.example.forzenbook.data.network.LoginService

class LoginRepositoryImpl(
    private val dao: LoginDao,
    private val service: LoginService
) : LoginRepository {
    override suspend fun getToken(email: String, password: String): LoginData? {
        //check database for token
        //if no token in data base get from service
        return try {
            LoginData(service.getToken().body()?.token)
        } catch (e: Exception) {
            null
        }
    }
}