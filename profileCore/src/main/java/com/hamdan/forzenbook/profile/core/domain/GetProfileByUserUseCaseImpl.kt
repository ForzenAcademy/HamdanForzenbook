package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel

class GetProfileByUserUseCaseImpl(
    private val repository: ProfileRepository
) : GetProfileByUserUseCase {
    override suspend fun invoke(userId: Int): BaseProfileViewModel.ProfileData {
        val info = repository.getProfile(userId)
        return BaseProfileViewModel.ProfileData(
            firstName = info.firstName,
            lastName = info.lastName,
            id = info.userId,
            isOwner = info.isOwner,
            postSet = info.postSet.map { it.toPostData() },
            userIconPath = info.userIconPath,
            dateJoined = info.dateJoined,
            aboutUser = info.aboutUser,
            firstPostId = info.postSet[0].postEntity.postId
        )
    }
}