package com.hamdan.forzenbook.profile.core.domain.mocks

import com.hamdan.forzenbook.core.GlobalConstants.PAGED_POSTS_SIZE
import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.profile.core.domain.GetOlderPostsUseCase

class MockGetOlderPostsUseCase : GetOlderPostsUseCase {
    override suspend fun invoke(userId: Int, postId: Int): List<PostData> {
        val x = mutableListOf<PostData>()
        val maxPosts = 30
        val listSize = if(maxPosts - PAGED_POSTS_SIZE > postId) PAGED_POSTS_SIZE else maxPosts - postId
        (1..listSize).forEach {
            x.add(
                PostData(
                    "Hamdan",
                    "Syed",
                    "Africa",
                    "https://cdn.dribbble.com/users/6255537/screenshots/14454071/media/0fd6c8dac9e38f5b5ca8ef6532eb703f.jpg?resize=400x0",
                    0,
                    postId + it,
                    "post id: ${postId + it}",
                    "text",
                    "2023-07-18 15:31:29"
                )
            )
        }
        return x
    }
}
