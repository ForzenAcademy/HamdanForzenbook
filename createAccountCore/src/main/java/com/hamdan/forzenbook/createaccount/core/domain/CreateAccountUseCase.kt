package com.hamdan.forzenbook.createaccount.core.domain

interface CreateAccountUseCase {
    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): CreateAccountResult
}
