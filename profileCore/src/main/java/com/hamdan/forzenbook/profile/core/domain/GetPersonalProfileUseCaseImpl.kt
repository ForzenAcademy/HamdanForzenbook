package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel.ProfileData

class GetPersonalProfileUseCaseImpl(
    private val repository: ProfileRepository
) : GetPersonalProfileUseCase {
    override suspend fun invoke(): ProfileData {
        val info = repository.getProfile(null)
        val postData = info.postSet.map { it.toPostData() }
        return ProfileData(
            firstName = info.firstName,
            lastName = info.lastName,
            id = info.userId,
            isOwner = info.isOwner,
            postSet = postData,
            userIconPath = info.userIconPath,
            dateJoined = info.dateJoined,
            aboutUser = info.aboutUser,
            firstPostId = if (postData.isEmpty()) null else postData[0].postId
        )
    }
}