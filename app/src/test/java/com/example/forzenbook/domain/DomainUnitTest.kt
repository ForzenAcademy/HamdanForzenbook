package com.example.forzenbook.domain

import com.hamdan.forzenbook.data.repository.AccountException
import com.hamdan.forzenbook.data.repository.CreateAccountRepository
import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.data.repository.NullTokenException
import com.hamdan.forzenbook.data.repository.User
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountResult
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.login.CreateAccountValidationUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.login.LoginGetStoredCredentialsUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.login.LoginStringValidationUseCaseImpl
import com.hamdan.forzenbook.domain.usecase.login.LoginValidationUseCaseImpl
import com.hamdan.forzenbook.view.login.LoginError
import com.hamdan.forzenbook.viewmodels.Entry
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Test

class DomainUnitTest {
    @Test
    fun `CreateAccountRequestUseCaseImpl test, checks all branches`() {
        val succeedRepo = mockk<CreateAccountRepository>()
        val accountExistsRepo = mockk<CreateAccountRepository>()
        val failureRepo = mockk<CreateAccountRepository>()

        coEvery { succeedRepo.createUser("", "", "", "", "") } returns Unit
        coEvery { accountExistsRepo.createUser("", "", "", "", "") } throws AccountException("")
        coEvery { failureRepo.createUser("", "", "", "", "") } throws Exception("")

        var useCase = CreateAccountUseCaseImpl(succeedRepo)
        runBlocking {
            Assert.assertEquals(useCase("", "", "", "", ""), CreateAccountResult.CREATE_SUCCESS)
        }
        useCase = CreateAccountUseCaseImpl(accountExistsRepo)
        runBlocking {
            Assert.assertEquals(useCase("", "", "", "", ""), CreateAccountResult.CREATE_EXISTS)
        }
        useCase = CreateAccountUseCaseImpl(failureRepo)
        runBlocking {
            Assert.assertEquals(useCase("", "", "", "", ""), CreateAccountResult.CREATE_FAILURE)
        }
    }

    @Test
    fun `RequestValidation Usecase test, checks that the invocation is successful`() {
        val successRepo = mockk<LoginRepository>()
        coEvery { successRepo.requestValidation("") } returns Unit
        val useCase = LoginValidationUseCaseImpl(successRepo)
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

        var useCase = LoginGetStoredCredentialsUseCaseImpl(loggedInRepo)
        runBlocking {
            try {
                useCase()
            } catch (e: Exception) {
                fail()
            }
        }
        useCase = LoginGetStoredCredentialsUseCaseImpl(notLoggedInRepo)
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
        val longEmail = "asdasdasdasdasdasdasdasdasdasd@gmail.com"
        val invalidEmailFormat = "a@a"
        val longAndInvalidEmail = "asdasdasdasdasdasdasdasdasdasdregwqe@s"
        val validEmail = "hamdan@gmail.com"
        val longCode = "1234567"
        val validCode = "123456"

        val useCase = LoginStringValidationUseCaseImpl()
        var state = LoginViewModel.LoginState(
            email = Entry(text = longEmail, error = LoginError.EmailError.Valid),
            code = Entry(text = validCode, error = LoginError.CodeError.Valid),
        )
        if (useCase(state).email.error != LoginError.EmailError.Length) fail()
        state = state.copy(email = Entry(invalidEmailFormat, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.InvalidFormat) fail()
        state = state.copy(email = Entry(longAndInvalidEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Length) fail()
        state = state.copy(email = Entry(validEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Valid) fail()

        state = state.copy(code = Entry(longCode, state.code.error))
        if (useCase(state).code.error != LoginError.CodeError.Length) fail()
        state = state.copy(code = Entry(validCode, state.code.error))
        if (useCase(state).code.error != LoginError.CodeError.Valid) fail()
    }

    @Test
    fun `Create Account String Validation Use Case test, checking correct update of all values and branches`() {
        val longName = "asbduiqwnejkasdoinwqeiujnaskdjniuwqnekjansdiunqwekjnasd"
        val invalidCharName = "asdijn123984rkjsnd!"
        val validName = "Hamdan"

        val longEmail = "asdasdasdasdasdasdasdasdasdasd@gmail.com"
        val invalidEmailFormat = "a@a"
        val longAndInvalidEmail = "asdasdasdasdasdasdasdasdasdasdregwqe@s"
        val validEmail = "hamdan@gmail.com"

        val longLocation = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        val validLocation = "California dab"

        val youngDate = "01-01-2022"
        val validDate = "07-09-1997"

        val useCase = CreateAccountValidationUseCaseImpl()
        var state = LoginViewModel.CreateAccountState(
            firstName = Entry(text = longName, error = LoginError.NameError.Length),
            lastName = Entry(text = validName, error = LoginError.NameError.Length),
            birthDay = Entry(text = validDate, error = LoginError.NameError.Length),
            email = Entry(text = validEmail, error = LoginError.NameError.Length),
            location = Entry(text = validLocation, error = LoginError.NameError.Length),
        )
        if (useCase(state).firstName.error != LoginError.NameError.Length) fail()
        state = state.copy(firstName = Entry(invalidCharName, state.firstName.error))
        if (useCase(state).firstName.error != LoginError.NameError.InvalidCharacters) fail()
        state = state.copy(firstName = Entry(validName, state.firstName.error))
        if (useCase(state).firstName.error != LoginError.NameError.Valid) fail()

        state = state.copy(lastName = Entry(longName, state.lastName.error))
        if (useCase(state).lastName.error != LoginError.NameError.Length) fail()
        state = state.copy(lastName = Entry(invalidCharName, state.lastName.error))
        if (useCase(state).lastName.error != LoginError.NameError.InvalidCharacters) fail()
        state = state.copy(lastName = Entry(validName, state.lastName.error))
        if (useCase(state).lastName.error != LoginError.NameError.Valid) fail()

        state = state.copy(email = Entry(longEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Length) fail()
        state = state.copy(email = Entry(invalidEmailFormat, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.InvalidFormat) fail()
        state = state.copy(email = Entry(longAndInvalidEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Length) fail()
        state = state.copy(email = Entry(validEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Valid) fail()

        state = state.copy(location = Entry(longLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Length) fail()
        state = state.copy(location = Entry(validLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Valid) fail()

        state = state.copy(location = Entry(longLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Length) fail()
        state = state.copy(location = Entry(validLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Valid) fail()

        state = state.copy(birthDay = Entry(youngDate, state.birthDay.error))
        if (useCase(state).birthDay.error != LoginError.BirthDateError.TooYoung) fail()
        state = state.copy(birthDay = Entry(validDate, state.birthDay.error))
        if (useCase(state).birthDay.error != LoginError.BirthDateError.Valid) fail()
    }
}
