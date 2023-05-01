package com.hamdan.forzenbook.createaccount.core.domain.mocks

import com.hamdan.forzenbook.core.AccountException
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepository
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCase

class MockCreateAccountUseCaseBadData(
    val repository: CreateAccountRepository
) : CreateAccountUseCase {
    override suspend fun invoke(
        firstName: String,
        lastName: String,
        birthDay: String,
        email: String,
        location: String
    ): CreateAccountResult {
        val badFirst = "a1z1"
        val badLast = "s1z2"
        val badEmail = "asdolmsd"
        return try {
            repository.createUser(badFirst, badLast, birthDay, badEmail, location)
            CreateAccountResult.CREATE_SUCCESS
        } catch (e: AccountException) {
            CreateAccountResult.CREATE_EXISTS
        } catch (e: Exception) {
            CreateAccountResult.CREATE_FAILURE
        }
    }
}
