package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.data.PagingDirection
import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.profile.core.data.repository.ProfileRepository

class GetNewerPostsUseCaseImpl(
    private val repository: ProfileRepository
) : GetNewerPostsUseCase {
    override suspend fun invoke(userId: Int, postId: Int): List<PostData> {
        return repository.getPagedPosts(postId, userId, PagingDirection.UP)
            .map { it.toPostData() }
    }
}
