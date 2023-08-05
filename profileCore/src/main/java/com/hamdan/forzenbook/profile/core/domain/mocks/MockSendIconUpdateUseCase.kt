package com.hamdan.forzenbook.profile.core.domain.mocks

import com.hamdan.forzenbook.profile.core.domain.SendIconUpdateUseCase

class MockSendIconUpdateUseCase:SendIconUpdateUseCase {
    override suspend fun invoke(userId: Int, iconPath: String): String {
        throw Exception()
    }
}
