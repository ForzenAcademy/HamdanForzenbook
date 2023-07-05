package com.hamdan.forzenbook.profile.core.domain

interface SendAboutUpdateUseCase {
    suspend operator fun invoke(userId: Int, about: String)
}