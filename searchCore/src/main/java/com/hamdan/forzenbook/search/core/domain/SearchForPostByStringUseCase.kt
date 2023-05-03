package com.hamdan.forzenbook.search.core.domain

interface SearchForPostByStringUseCase {
    suspend operator fun invoke(query: String, token: String)
}
