package com.hamdan.forzenbook.domain.usecase.login

interface LoginGetCredentialsFromNetworkUseCase {
    suspend operator fun invoke(email: String, code: String)
}
