package com.hamdan.forzenbook.domain.usecase.login

interface LoginValidationUseCase {
    suspend operator fun invoke(email: String)
}
