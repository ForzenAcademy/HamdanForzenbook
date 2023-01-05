package com.hamdan.forzenbook.data.repository

import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.network.LoginService

class LoginRepositoryImpl(
    private val dao: LoginDao,
    private val service: LoginService
) : LoginRepository {
    override suspend fun getToken(email: String): LoginData? {
        //check database for token
        //if no token in data base get from service
        return try {
            LoginData(service.getToken().body()?.token)
        } catch (e: Exception) {
            null
        }
    }
}