package com.hamdan.forzenbook.profile.core.domain

import com.hamdan.forzenbook.core.PostData

interface GetForwardPostsUseCase {
    suspend operator fun invoke(userId: Int, postId: Int): List<PostData>
}
