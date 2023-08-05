package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.core.PostException
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository
import java.io.File

class SendIconUpdateUseCaseImpl(
    private val repository: ProfileRepository
) : SendIconUpdateUseCase {
    override suspend fun invoke(userId: Int, iconPath: String):String {
        val file = File(iconPath)
        if (file.exists()) {
            return repository.updateIcon(userId, file).image
        } else {
            throw PostException("File attempted to post does not exist")
        }
    }
}