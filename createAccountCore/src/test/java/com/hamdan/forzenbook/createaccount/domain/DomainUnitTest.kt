package com.hamdan.forzenbook.createaccount.domain

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError
import com.hamdan.forzenbook.createaccount.core.data.repository.AccountException
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepository
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountEntrys
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountResult
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountUseCaseImpl
import com.hamdan.forzenbook.createaccount.core.domain.CreateAccountValidationUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert

class DomainUnitTest {
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

    fun `Create Account String Validation Use Case test, checking correct update of all values and branches`() {
        val longName = "asbduiqwnejkasdoinwqeiujnaskdjniuwqnekjansdiunqwekjnasd"
        val invalidCharName = "asdijn123984rkjsnd!"
        val validName = "Hamdan"
        val longEmail = "asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd@gmail.com"
        val invalidEmailFormat = "a@a"
        val longAndInvalidEmail =
            "asdasdasdasdasdasdasdasdasdasdregwqeasdasdasdasdasdasdasdasdasdasdregwqe@s"
        val validEmail = "hamdan@gmail.com"

        val longLocation = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        val validLocation = "California dab"

        val youngDate = "01-01-2022"
        val validDate = "07-09-1997"

        val useCase = CreateAccountValidationUseCaseImpl()
        var state = CreateAccountEntrys(
            firstName = Entry(text = longName, error = LoginError.NameError.Length),
            lastName = Entry(text = validName, error = LoginError.NameError.Length),
            birthDay = Entry(text = validDate, error = LoginError.NameError.Length),
            email = Entry(text = validEmail, error = LoginError.NameError.Length),
            location = Entry(text = validLocation, error = LoginError.NameError.Length),
        )
        if (useCase(state).firstName.error != LoginError.NameError.Length) Assert.fail()
        state = state.copy(firstName = Entry(invalidCharName, state.firstName.error))
        if (useCase(state).firstName.error != LoginError.NameError.InvalidCharacters) Assert.fail()
        state = state.copy(firstName = Entry(validName, state.firstName.error))
        if (useCase(state).firstName.error != LoginError.NameError.Valid) Assert.fail()

        state = state.copy(lastName = Entry(longName, state.lastName.error))
        if (useCase(state).lastName.error != LoginError.NameError.Length) Assert.fail()
        state = state.copy(lastName = Entry(invalidCharName, state.lastName.error))
        if (useCase(state).lastName.error != LoginError.NameError.InvalidCharacters) Assert.fail()
        state = state.copy(lastName = Entry(validName, state.lastName.error))
        if (useCase(state).lastName.error != LoginError.NameError.Valid) Assert.fail()

        state = state.copy(email = Entry(longEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Length) Assert.fail()
        state = state.copy(email = Entry(invalidEmailFormat, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.InvalidFormat) Assert.fail()
        state = state.copy(email = Entry(longAndInvalidEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Length) Assert.fail()
        state = state.copy(email = Entry(validEmail, state.email.error))
        if (useCase(state).email.error != LoginError.EmailError.Valid) Assert.fail()

        state = state.copy(location = Entry(longLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Length) Assert.fail()
        state = state.copy(location = Entry(validLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Valid) Assert.fail()

        state = state.copy(location = Entry(longLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Length) Assert.fail()
        state = state.copy(location = Entry(validLocation, state.location.error))
        if (useCase(state).location.error != LoginError.LocationError.Valid) Assert.fail()

        state = state.copy(birthDay = Entry(youngDate, state.birthDay.error))
        if (useCase(state).birthDay.error != LoginError.BirthDateError.TooYoung) Assert.fail()
        state = state.copy(birthDay = Entry(validDate, state.birthDay.error))
        if (useCase(state).birthDay.error != LoginError.BirthDateError.Valid) Assert.fail()
    }
}
