package com.hamdan.forzenbook.login.core.domain.usecase

interface LoginGetStoredCredentialsUseCase {
    suspend operator fun invoke()
}
