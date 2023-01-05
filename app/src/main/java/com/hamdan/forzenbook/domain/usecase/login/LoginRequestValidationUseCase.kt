package com.hamdan.forzenbook.domain.usecase.login

interface LoginRequestValidationUseCase {
    suspend operator fun invoke(email: String)
}
