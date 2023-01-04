package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.CreateAccountRepository

class CreateAccountRequestUseCaseImpl(
    val repository: CreateAccountRepository
) : CreateAccountRequestUseCase {
        override suspend fun invoke(
            firstName: String,
            lastName: String,
            birthDay: String,
            email: String,
            password: String,
            location: String
        ): Int {
            return repository.createUser(firstName, lastName, birthDay, email, password, location)
        }
}