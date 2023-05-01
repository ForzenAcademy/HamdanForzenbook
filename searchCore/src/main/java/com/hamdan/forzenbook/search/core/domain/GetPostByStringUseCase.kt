package com.hamdan.forzenbook.search.core.domain

import com.hamdan.forzenbook.core.PostData

interface GetPostByStringUseCase {
    suspend operator fun invoke(query: String, token: String): List<PostData>
}
