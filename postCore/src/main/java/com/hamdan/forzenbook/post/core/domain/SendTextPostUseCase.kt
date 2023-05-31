package com.hamdan.forzenbook.post.core.domain

interface SendTextPostUseCase {
    suspend operator fun invoke(message: String)
}
