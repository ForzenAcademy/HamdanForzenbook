package com.hamdan.forzenbook.domain.usecase.login

interface CreateAccountRequestUseCase {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): Int

}