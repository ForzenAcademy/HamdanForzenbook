package com.example.forzenbook.domain.usecase.mocks

import com.example.forzenbook.domain.usecase.login.ForgotPasswordResetUseCase

class MockForgotPasswordResetUseCaseFails : ForgotPasswordResetUseCase {
    override suspend fun invoke(email: String): Int {
        throw RuntimeException("Something went wrong when requesting reset")
    }
}