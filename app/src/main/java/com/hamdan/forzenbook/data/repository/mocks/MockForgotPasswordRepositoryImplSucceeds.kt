package com.hamdan.forzenbook.data.repository.mocks

import com.hamdan.forzenbook.data.network.ForgotPasswordService
import com.hamdan.forzenbook.data.repository.ForgotPasswordRepository

class MockForgotPasswordRepositoryImplSucceeds(
    forgotPasswordService: ForgotPasswordService
) : ForgotPasswordRepository {
    override suspend fun requestReset(email: String): Int {
        return 200
    }
}