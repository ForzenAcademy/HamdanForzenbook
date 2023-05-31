package com.hamdan.forzenbook.post.core.domain

import com.hamdan.forzenbook.post.core.data.repository.PostRepository

class SendTextPostUseCaseImpl(
    private val repository: PostRepository
) : SendTextPostUseCase {
    override suspend fun invoke(message: String) {
        repository.postText(message)
    }
}
