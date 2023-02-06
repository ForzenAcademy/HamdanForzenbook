package com.hamdan.forzenbook.login.core.domain.usecase

interface LoginGetCredentialsFromNetworkUseCase {
    suspend operator fun invoke(email: String, code: String)
}
