package com.hamdan.forzenbook.domain.usecase.login

interface LoginUseCaseGetCredentialsFromNetwork {
    suspend operator fun invoke(email: String, code: String)
}
