package com.hamdan.forzenbook.search.core.domain

import com.hamdan.forzenbook.search.core.data.repository.SearchRepository

class SearchForPostByStringUseCaseImpl(
    private val repository: SearchRepository,
) : SearchForPostByStringUseCase {
    override suspend fun invoke(query: String) {
        repository.searchPostByQuery(query)
    }
}
