package com.hamdan.forzenbook.data.repository.mocks

import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.network.LoginService
import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.data.repository.User

class MockGetsToken(
    private val loginDao: LoginDao,
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun requestValidation(email: String) {
    }

    override suspend fun getToken(email: String?, code: String?): User {
        return User.LoggedIn
    }
}
