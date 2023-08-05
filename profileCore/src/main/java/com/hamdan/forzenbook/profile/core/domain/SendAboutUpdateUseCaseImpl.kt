package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository

class SendAboutUpdateUseCaseImpl(
    val repository: ProfileRepository
) : SendAboutUpdateUseCase {
    override suspend fun invoke(userId:Int, about: String) {
        repository.updateAboutMe(userId,about)
    }
}