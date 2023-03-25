package com.hamdan.forzenbook.post.core.domain

interface SendImagePostUseCase {
    suspend operator fun invoke(token: String, filePath: String)
}
