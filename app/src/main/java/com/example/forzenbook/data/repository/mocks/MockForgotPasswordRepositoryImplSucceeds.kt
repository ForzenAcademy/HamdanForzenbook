package com.example.forzenbook.data.repository.mocks

import com.example.forzenbook.data.network.ForgotPasswordService
import com.example.forzenbook.data.repository.ForgotPasswordRepository

class MockForgotPasswordRepositoryImplSucceeds(
    forgotPasswordService: ForgotPasswordService
) : ForgotPasswordRepository {
    override suspend fun requestReset(email: String): Int {
        return 200
    }
}