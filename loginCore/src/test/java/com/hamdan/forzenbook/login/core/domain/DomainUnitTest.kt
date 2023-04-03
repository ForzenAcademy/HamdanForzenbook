package com.hamdan.forzenbook.login.core.domain

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.login.core.data.repository.LoginRepository
import com.hamdan.forzenbook.login.core.data.repository.NullTokenException
import com.hamdan.forzenbook.login.core.data.repository.User
import com.hamdan.forzenbook.login.core.domain.usecase.LoginEntrys
import com.hamdan.forzenbook.login.core.domain.usecase.LoginGetStoredCredentialsUseCaseImpl
import com.hamdan.forzenbook.login.core.domain.usecase.LoginStringValidationUseCaseImpl
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

        coEvery { loggedInRepo.getToken() } returns User.LoggedIn
        coEvery { notLoggedInRepo.getToken() } returns User.NotLoggedIn

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

    @Test
    fun `Login String Validation Use Case test, checking correct update of all values and branches`() {
        val longEmail = "asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd@gmail.com"
        val invalidEmailFormat = "a@a"
        val longAndInvalidEmail = "asdasdasdasdasdasdasdasdasdasdregwqeasdasdasdasdasdasdasdasdasdasdregwqe@s"
        val validEmail = "hamdan@gmail.com"
        val longCode = "1234567"
        val validCode = "123456"

        val useCase =
            LoginStringValidationUseCaseImpl()
        var state = LoginEntrys(
            email = Entry(
                text = longEmail,
                error = EntryError.EmailError.Valid
            ),
            code = Entry(text = validCode, error = EntryError.CodeError.Valid),
        )
        if (useCase(state).email.error != EntryError.EmailError.Length) fail()
        state = state.copy(email = Entry(invalidEmailFormat, state.email.error))
        if (useCase(state).email.error != EntryError.EmailError.InvalidFormat) fail()
        state = state.copy(email = Entry(longAndInvalidEmail, state.email.error))
        if (useCase(state).email.error != EntryError.EmailError.Length) fail()
        state = state.copy(email = Entry(validEmail, state.email.error))
        if (useCase(state).email.error != EntryError.EmailError.Valid) fail()

        state = state.copy(code = Entry(longCode, state.code.error))
        if (useCase(state).code.error != EntryError.CodeError.Length) fail()
        state = state.copy(code = Entry(validCode, state.code.error))
        if (useCase(state).code.error != EntryError.CodeError.Valid) fail()
    }
}
