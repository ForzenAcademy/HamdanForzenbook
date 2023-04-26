package com.hamdan.forzenbook.search.core.domain

import com.hamdan.forzenbook.search.core.data.repository.SearchRepository

class SearchForPostByIdUseCaseImpl(
    private val repository: SearchRepository,
) : SearchForPostByIdUseCase {
    override suspend fun invoke(id: Int) {
        repository.searchPostByUserId(id)
    }
}
