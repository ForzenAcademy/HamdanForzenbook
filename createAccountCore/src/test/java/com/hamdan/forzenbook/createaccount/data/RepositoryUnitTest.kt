package com.hamdan.forzenbook.createaccount.data

import android.accounts.NetworkErrorException
import com.hamdan.forzenbook.createaccount.core.data.network.CreateAccountService
import com.hamdan.forzenbook.createaccount.core.data.repository.AccountException
import com.hamdan.forzenbook.createaccount.core.data.repository.CreateAccountRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.sql.Date

class RepositoryUnitTest {
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
                Assert.fail()
            }
        }
        repository = CreateAccountRepositoryImpl(userExistsService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
                Assert.fail()
            } catch (e: Exception) {
                if (e !is AccountException) Assert.fail()
            }
        }
        repository = CreateAccountRepositoryImpl(errorService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
                Assert.fail()
            } catch (e: Exception) {
                if (e is AccountException) Assert.fail()
            }
        }
        repository = CreateAccountRepositoryImpl(networkErrorService)
        runBlocking {
            try {
                repository.createUser("", "", "2020-01-01", "", "")
                Assert.fail()
            } catch (e: Exception) {
                println(e)
                if (e !is NetworkErrorException) Assert.fail()
            }
        }
    }
}
