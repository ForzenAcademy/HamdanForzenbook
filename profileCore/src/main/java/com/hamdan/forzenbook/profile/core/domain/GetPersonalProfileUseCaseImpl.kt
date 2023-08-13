package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.convertDate
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
            userId = info.userId,
            isOwner = info.isOwner,
            postSet = postData,
            userIconPath = info.userIconPath,
            dateJoined = convertDate(info.dateJoined),
            aboutUser = info.aboutUser,
            firstPostId = if (postData.isEmpty()) null else postData[0].postId,
            lastPostId = if (postData.size < GlobalConstants.POSTS_MAX_SIZE && postData.isNotEmpty()) postData[postData.lastIndex].postId else null,
        )
    }
}
