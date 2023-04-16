package com.hamdan.forzenbook.login.core.domain

import com.hamdan.forzenbook.login.core.data.repository.LoginRepository
import com.hamdan.forzenbook.login.core.data.repository.NullTokenException
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCaseImpl
import com.hamdan.forzenbook.login.core.domain.usecase.LoginValidationUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.fail
import org.junit.Test

class DomainUnitTest {
    @Test
    fun `RequestValidation Usecase test, checks that the invocation is successful`() {
        val successRepo = mockk<LoginRepository>()
        coEvery { successRepo.requestValidation("") } returns Unit
        val useCase =
            LoginValidationUseCaseImpl(successRepo)
        runBlocking {
            try {
                useCase("")
            } catch (e: Exception) {
                fail()
            }
        }
    }

    @Test
    fun `LoginUsecase get token from DB test, checks success and whether it properly throws an Exception`() {
        val loggedInRepo = mockk<LoginRepository>()
        val notLoggedInRepo = mockk<LoginRepository>()
        val successResponse = "itsatoken"
        val errorResponse = Exception()

        coEvery { loggedInRepo.getToken() } returns "itsatoken"
        coEvery { notLoggedInRepo.getToken() } returns null

        var useCase =
            LoginGetStoredCredentialsUseCaseImpl(
                loggedInRepo
            )
        runBlocking {
            try {
                useCase()
            } catch (e: Exception) {
                fail()
            }
        }
        useCase =
            LoginGetStoredCredentialsUseCaseImpl(
                notLoggedInRepo
            )
        runBlocking {
            try {
                useCase()
                fail()
            } catch (e: Exception) {
                if (e !is NullTokenException) fail()
            }
        }
    }
}
