package com.hamdan.forzenbook.createaccount.core.domain

import android.util.Log
import com.hamdan.forzenbook.core.AccountException
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepository

class CreateAccountUseCaseImpl(
    val repository: CreateAccountRepository
) : CreateAccountUseCase {
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
            Log.v("Hamdan", e.stackTraceToString())
            CreateAccountResult.CREATE_FAILURE
        }
    }
}

enum class CreateAccountResult {
    CREATE_SUCCESS,
    CREATE_EXISTS,
    CREATE_FAILURE,
}
