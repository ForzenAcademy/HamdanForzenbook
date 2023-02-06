package com.hamdan.forzenbook.login.core.domain.usecase

interface LoginValidationUseCase {
    suspend operator fun invoke(email: String)
}
