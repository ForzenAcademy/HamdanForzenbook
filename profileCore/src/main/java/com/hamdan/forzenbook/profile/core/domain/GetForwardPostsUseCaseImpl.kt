package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.data.PagingDirection
import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository

class GetForwardPostsUseCaseImpl(
    private val repository: ProfileRepository
) : GetForwardPostsUseCase {
    override suspend fun invoke(userId: Int, postId: Int): List<PostData> {
        return repository.getPagedPosts(postId, userId, PagingDirection.FORWARD)
            .map { it.toPostData() }
    }
}
