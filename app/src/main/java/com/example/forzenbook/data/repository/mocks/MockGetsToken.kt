package com.example.forzenbook.data.repository.mocks

import com.example.forzenbook.data.database.LoginDao
import com.example.forzenbook.data.network.LoginService
import com.example.forzenbook.data.repository.LoginData
import com.example.forzenbook.data.repository.LoginRepository

class MockGetsToken(
    private val loginDao: LoginDao,
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun getToken(email: String, password: String): LoginData? {
        return LoginData("it worked")
    }
}