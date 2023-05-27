package com.hamdan.forzenbook.post.core.domain

interface SendImagePostUseCase {
    suspend operator fun invoke(filePath: String)
}
