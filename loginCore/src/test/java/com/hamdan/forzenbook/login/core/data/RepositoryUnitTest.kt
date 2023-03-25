package com.hamdan.forzenbook.login.core.data

import android.accounts.NetworkErrorException
import com.hamdan.forzenbook.login.core.data.network.LoginResponse
import com.hamdan.forzenbook.login.core.data.network.LoginService
import com.hamdan.forzenbook.login.core.data.repository.FailTokenRetrievalException
import com.hamdan.forzenbook.login.core.data.repository.LoginRepositoryImpl
import com.hamdan.forzenbook.login.core.data.repository.NullTokenException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.Response

class RepositoryUnitTest {
    @Test
    fun `LoginRepository test, checking requestValidation success case`() {
        val succeedsService = mockk<LoginService>()
        val networkErrorService = mockk<LoginService>()
        coEvery { succeedsService.requestValidation("") } returns Response.success(null)
        coEvery { networkErrorService.requestValidation("") } throws NetworkErrorException()
        var repository = LoginRepositoryImpl(succeedsService)
        runBlocking {
            try {
                repository.requestValidation("")
            } catch (e: Exception) {
                fail()
            }
        }
        repository = LoginRepositoryImpl(networkErrorService)
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
        val succeedsService = mockk<LoginService>()
        val errorService = mockk<LoginService>()
        val nullBodyService = mockk<LoginService>()
        val nullTokenService = mockk<LoginService>()
        val networkErrorService = mockk<LoginService>()
        val successToken = "itsatoken"

        coEvery { succeedsService.getToken("", "") } returns Response.success(
            LoginResponse(
                successToken
            )
        )
        coEvery { errorService.getToken("", "") } returns Response.error(
            400,
            ResponseBody.create(null, "")
        )
        coEvery { nullBodyService.getToken("", "") } returns Response.success(null)
        coEvery { nullTokenService.getToken("", "") } returns Response.success(LoginResponse(null))
        coEvery { networkErrorService.getToken("", "") } throws NetworkErrorException()

        var repository = LoginRepositoryImpl(succeedsService)
        runBlocking {
            try {
                if (repository.getToken("", "") != successToken) fail()
            } catch (e: Exception) {
                println(e)
                fail()
            }
        }

        repository = LoginRepositoryImpl(nullBodyService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is FailTokenRetrievalException) fail()
            }
        }

        repository = LoginRepositoryImpl(errorService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is FailTokenRetrievalException) fail()
            }
        }

        repository = LoginRepositoryImpl(nullTokenService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is NullTokenException) fail()
            }
        }
        repository = LoginRepositoryImpl(networkErrorService)
        runBlocking {
            try {
                repository.getToken("", "")
                fail()
            } catch (e: Exception) {
                if (e !is NetworkErrorException) fail()
            }
        }
    }
}
