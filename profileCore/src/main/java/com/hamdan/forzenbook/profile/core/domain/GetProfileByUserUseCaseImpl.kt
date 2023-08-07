package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.convertDate
import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository
import com.hamdan.forzenbook.profile.core.viewmodel.BaseProfileViewModel

class GetProfileByUserUseCaseImpl(
    private val repository: ProfileRepository
) : GetProfileByUserUseCase {
    override suspend fun invoke(userId: Int): BaseProfileViewModel.ProfileData {
        val info = repository.getProfile(userId)
        val postData = info.postSet.map { it.toPostData() }
        return BaseProfileViewModel.ProfileData(
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
