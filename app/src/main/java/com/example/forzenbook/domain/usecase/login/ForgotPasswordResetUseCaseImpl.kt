package com.example.forzenbook.domain.usecase.login

import com.example.forzenbook.data.repository.ForgotPasswordRepository

class ForgotPasswordResetUseCaseImpl(
    val repository: ForgotPasswordRepository
) : ForgotPasswordResetUseCase {
    override suspend fun invoke(email: String): Int {
        return repository.requestReset(email)
    }
}