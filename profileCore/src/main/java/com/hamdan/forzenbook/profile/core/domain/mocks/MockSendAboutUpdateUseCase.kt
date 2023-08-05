package com.hamdan.forzenbook.profile.core.domain.mocks

import com.hamdan.forzenbook.profile.core.domain.SendAboutUpdateUseCase

class MockSendAboutUpdateUseCase:SendAboutUpdateUseCase {
    override suspend fun invoke(userId: Int, about: String) {
        throw Exception()
    }
}
