package com.example.forzenbook.data

import android.accounts.NetworkErrorException
import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.database.LoginEntity
import com.hamdan.forzenbook.data.network.CreateAccountService
import com.hamdan.forzenbook.data.network.LoginResponse
import com.hamdan.forzenbook.data.network.LoginService
import com.hamdan.forzenbook.data.repository.AccountException
import com.hamdan.forzenbook.data.repository.CreateAccountRepositoryImpl
import com.hamdan.forzenbook.data.repository.FailTokenRetrievalException
import com.hamdan.forzenbook.data.repository.LoginRepositoryImpl
import com.hamdan.forzenbook.data.repository.NullTokenException
import com.hamdan.forzenbook.data.repository.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Response
import java.sql.Date

class RepositoryUnitTest {
    @Test
    fun `LoginRepository test, checking requestValidation success case`() {
        val succeedsService = mockk<LoginService>()
        val networkErrorService = mockk<LoginService>()
        val succeedsDao = mockk<LoginDao>()
        coEvery { succeedsService.requestValidation("") } returns Response.success(null)
        coEvery { networkErrorService.requestValidation("") } throws NetworkErrorException()
        var repository = LoginRepositoryImpl(succeedsDao, succeedsService)
        runBlocking {
            try {
                repository.requestValidation("")
            } catch (e: Exception) {
                fail()
            }
        }
        repository = LoginRepositoryImpl(succeedsDao, networkErrorService)
        runBlocking {
            try {
                repository.requestValidation("")
                fail()
            } catch (e: Exception) {
                if (e !is NetworkErrorException) fail()
            }
        }
    }

    @Test
    fun `LoginRepository test, checking getToken From Network cases as well as proper throwing`() {
        val dao = mockk<LoginDao>()
        val succeedsService = mockk<LoginService>()
        val errorService = mockk<LoginService>()
        val nullBodyService = mockk<LoginService>()
        val nullTokenService = mockk<LoginService>()
        val networkErrorService = mockk<LoginService>()

        coEvery { succeedsService.getToken("", "") } returns Response.success(LoginResponse(""))
        coEvery { errorService.getToken("", "") } returns Response.error(
            400,
            ResponseBody.create(null, "")
        )
        coEvery { nullBodyService.getToken("", "") } returns Response.success(null)
        coEvery { nullTokenService.getToken("", "") } returns Response.success(LoginResponse(null))
        coEvery { networkErrorService.getToken("", "") } throws NetworkErrorException()
        coEvery { dao.insert(LoginEntity("", "")) } returns Unit

        var repository = LoginRepositoryImpl(dao, succeedsService)
        runBlocking {
            try {
                if (repository.getToken("", "") != User.LoggedIn) fail()
            } catch (e: Exception) {
                println(e)
                fail()
            }
        }

        repository = LoginRepositoryImpl(dao, nullBodyService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is FailTokenRetrievalException) fail()
            }
        }

        repository = LoginRepositoryImpl(dao, errorService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is FailTokenRetrievalException) fail()
            }
        }

        repository = LoginRepositoryImpl(dao, nullTokenService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is NullTokenException) fail()
            }
        }
        repository = LoginRepositoryImpl(dao, networkErrorService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is NetworkErrorException) fail()
            }
        }
    }

    @Test
    fun `LoginRepository test, checking getToken From Database cases`() {
        val succeedsDao = mockk<LoginDao>()
        val nullTokenDao = mockk<LoginDao>()
        val service = mockk<LoginService>()

        coEvery { succeedsDao.getToken() } returns LoginEntity("", "")
        coEvery { nullTokenDao.getToken() } returns null

        var repository = LoginRepositoryImpl(succeedsDao, service)
        runBlocking {
            try {
                if (repository.getToken() != User.LoggedIn) fail()
            } catch (e: Exception) {
                fail()
            }
        }
        repository = LoginRepositoryImpl(nullTokenDao, service)
        runBlocking {
            try {
                if (repository.getToken() != User.NotLoggedIn) fail()
            } catch (e: Exception) {
                fail()
            }
        }
    }

    @Test
    fun `CreateAccount Repository test, checking success case, and making sure throws are correct`() {
        val succeedsService = mockk<CreateAccountService>()
        val userExistsService = mockk<CreateAccountService>()
        val errorService = mockk<CreateAccountService>()
        val networkErrorService = mockk<CreateAccountService>()

        coEvery {
            succeedsService.createUser(
                "",
                Date.valueOf("2020-01-01"),
                "",
                "",
                ""
            )
        } returns Response.success(null)
        coEvery {
            userExistsService.createUser(
                "",
                Date.valueOf("2020-01-01"),
                "",
                "",
                ""
            )
        } returns Response.error(
            409,
            ResponseBody.create(null, "")
        )
        coEvery {
            errorService.createUser(
                "",
                Date.valueOf("2020-01-01"),
                "",
                "",
                ""
            )
        } returns Response.error(
            400,
            ResponseBody.create(null, "")
        )
        coEvery {
            networkErrorService.createUser(
                "",
                Date.valueOf("2020-01-01"),
                "",
                "",
                ""
            )
        } throws NetworkErrorException()

        var repository = CreateAccountRepositoryImpl(succeedsService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
            } catch (e: Exception) {
                fail()
            }
        }
        repository = CreateAccountRepositoryImpl(userExistsService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
                fail()
            } catch (e: Exception) {
                if (e !is AccountException) fail()
            }
        }
        repository = CreateAccountRepositoryImpl(errorService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
                fail()
            } catch (e: Exception) {
                if (e is AccountException) fail()
            }
        }
        repository = CreateAccountRepositoryImpl(networkErrorService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
                fail()
            } catch (e: Exception) {
                println(e)
                if (e !is NetworkErrorException) fail()
            }
        }
    }
}
