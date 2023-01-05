package com.hamdan.forzenbook.domain.usecase.login

interface LoginGetTokenUseCase {
    suspend operator fun invoke(email: String, code: String)
}
