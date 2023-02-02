package com.hamdan.forzenbook.domain.usecase.login

interface LoginUseCaseValidation {
    suspend operator fun invoke(email: String)
}
