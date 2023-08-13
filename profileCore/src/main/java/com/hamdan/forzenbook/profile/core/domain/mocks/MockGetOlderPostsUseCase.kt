package com.hamdan.forzenbook.profile.core.domain.mocks

import com.hamdan.forzenbook.core.GlobalConstants.PAGED_POSTS_SIZE
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.profile.core.domain.GetOlderPostsUseCase

class MockGetOlderPostsUseCase : GetOlderPostsUseCase {
    override suspend fun invoke(userId: Int, postId: Int): List<PostData> {
        val x = mutableListOf<PostData>()
        (1..PAGED_POSTS_SIZE).forEach {
            x.add(
                PostData(
                    "Hamdan",
                    "Syed",
                    "Africa",
                    "upload/pp_default.jpg",
                    0,
                    postId + it,
                    "${postId + it}",
                    "text",
                    "some day"
                )
            )
        }
        return x
    }
}