package com.hamdan.forzenbook.search.core.domain

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.search.core.data.repository.SearchRepository

class GetPostByUserIdUseCaseImpl(
    private val repository: SearchRepository,
) : GetPostByUserIdUseCase {
    override suspend fun invoke(userId: Int): List<PostData> {
        return repository.getPostByUserId(userId).map { it.toPostData() }
    }
}
