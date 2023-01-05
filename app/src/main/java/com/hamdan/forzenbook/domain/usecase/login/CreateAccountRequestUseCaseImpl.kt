package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.AccountException
import com.hamdan.forzenbook.data.repository.CreateAccountRepository

class CreateAccountRequestUseCaseImpl(
    val repository: CreateAccountRepository
) : CreateAccountRequestUseCase {
    override suspend fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): CreateAccountResult {
        return try {
            repository.createUser(firstName, lastName, birthDay, email, location)
            CreateAccountResult.CREATE_SUCCESS
        } catch (e: AccountException) {
            CreateAccountResult.CREATE_EXISTS
        } catch (e: Exception) {
            CreateAccountResult.CREATE_FAILURE
        }
    }
}
