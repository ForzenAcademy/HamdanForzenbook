package com.hamdan.forzenbook.search.core.domain

import com.hamdan.forzenbook.core.PostData

interface GetPostByUserIdUseCase {
    suspend operator fun invoke(userId: Int): List<PostData>
}
