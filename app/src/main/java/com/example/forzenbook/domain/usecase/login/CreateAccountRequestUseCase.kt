package com.example.forzenbook.domain.usecase.login

interface CreateAccountRequestUseCase {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        password: String,
        location: String
    ): Int

}