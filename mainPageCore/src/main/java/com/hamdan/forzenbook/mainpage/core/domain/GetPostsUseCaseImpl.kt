package com.hamdan.forzenbook.mainpage.core.domain

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.mainpage.core.data.repository.FeedRepository

class GetPostsUseCaseImpl(
    private val repository: FeedRepository
) : GetPostsUseCase {
    override suspend fun invoke(nameFormat: String, token: String): List<PostData> {
        return repository.getFeed(nameFormat, token).map { it.toPostData() }
    }
}
