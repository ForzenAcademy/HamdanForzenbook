package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginRepository

class LoginGetTokenUseCaseImpl(
    val repository: LoginRepository
) : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, code: String) {
        // TODO check database for valid token
        // goes here

        // TODO in FA-84 instead of returning the token to the VM, save the token in the database
        // have the vm send off to wherever if there is no exception, and have that place access the token from database
        repository.getToken(email, code)?.token ?: throw Exception("null token")

        // Update Database with token here
    }
}
