package com.hamdan.forzenbook.mainpage.core.domain

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.mainpage.core.data.repository.FeedRepository
import com.hamdan.forzenbook.mainpage.core.data.repository.toPostData

class GetPostsUseCaseImpl(
    private val repository: FeedRepository
) : GetPostsUseCase {
    override suspend fun invoke(nameFormat: String): List<PostData> {
        return repository.getFeed(nameFormat).map {
            it.toPostData()
        }
    }
}
