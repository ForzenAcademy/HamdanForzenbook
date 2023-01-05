package com.hamdan.forzenbook.data.network.mocks

import com.hamdan.forzenbook.data.network.ForgotPasswordService
import retrofit2.Response

class MockForgotPasswordServiceError:ForgotPasswordService {
    override suspend fun requestReset(): Response<Any> {
        throw RuntimeException("There was an issue!")
    }
}