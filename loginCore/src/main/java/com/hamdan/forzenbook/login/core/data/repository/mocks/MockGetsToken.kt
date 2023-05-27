package com.hamdan.forzenbook.login.core.data.repository.mocks

import com.hamdan.forzenbook.login.core.data.network.LoginService
import com.hamdan.forzenbook.login.core.data.repository.LoginRepository

class MockGetsToken(
    private val loginService: LoginService
) : LoginRepository {
    override suspend fun requestValidation(email: String) {
    }

    override suspend fun getToken(email: String?, code: String?) {
    }
}
