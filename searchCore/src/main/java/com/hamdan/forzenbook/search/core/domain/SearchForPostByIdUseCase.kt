package com.hamdan.forzenbook.search.core.domain

interface SearchForPostByIdUseCase {
    suspend operator fun invoke(id: Int, token: String)
}
