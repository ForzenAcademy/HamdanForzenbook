package com.hamdan.forzenbook.mainpage.core.domain

import com.hamdan.forzenbook.core.PostData

interface GetPostsUseCase {
    suspend operator fun invoke(): List<PostData>
}
