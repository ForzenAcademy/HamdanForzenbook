package com.hamdan.forzenbook.search.core.domain

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.data.entities.toPostData
import com.hamdan.forzenbook.search.core.data.repository.SearchRepository

/**
 * get the posts by a string search and convert it to post data
 */
class GetPostByStringUseCaseImpl(
    private val repository: SearchRepository
) : GetPostByStringUseCase {
    override suspend fun invoke(query: String): List<PostData> {
        return repository.getPostByQuery(query).map { it.toPostData() }
    }
}
