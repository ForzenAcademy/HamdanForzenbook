package com.hamdan.forzenbook.domain.usecase.mocks

import com.hamdan.forzenbook.data.repository.LoginRepository
import com.hamdan.forzenbook.domain.usecase.login.LoginGetTokenUseCase

class MockLoginGetTokenUseCaseSuccess(
    val repository: LoginRepository
) : LoginGetTokenUseCase {
    override suspend fun invoke(email: String, code: String) {
        // TODO (FA-84) implement a better mock? Maybe ask later about
        // adding some autopass in for admin with some string for testing
    }
}
