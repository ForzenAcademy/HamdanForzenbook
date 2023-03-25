package com.hamdan.forzenbook.post.core.domain

import com.hamdan.forzenbook.post.core.data.repository.PostRepository

class SendImagePostUseCaseImpl(
    private val repository: PostRepository
) : SendImagePostUseCase {
    override suspend fun invoke(token: String) {
        repository.postImage(token)
    }
}
