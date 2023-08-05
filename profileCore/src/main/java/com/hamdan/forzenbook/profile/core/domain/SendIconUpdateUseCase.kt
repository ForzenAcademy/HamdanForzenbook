package com.hamdan.forzenbook.profile.core.domain

interface SendIconUpdateUseCase {
    suspend operator fun invoke(userId: Int, iconPath: String): String
}