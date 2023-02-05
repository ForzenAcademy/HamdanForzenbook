package com.hamdan.forzenbook.domain.usecase.login

interface LoginGetStoredCredentialsUseCase {
    suspend operator fun invoke()
}
