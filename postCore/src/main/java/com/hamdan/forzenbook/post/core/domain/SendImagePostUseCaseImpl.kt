package com.hamdan.forzenbook.post.core.domain

import com.hamdan.forzenbook.core.PostException
import com.hamdan.forzenbook.post.core.data.repository.PostRepository
import java.io.File

class SendImagePostUseCaseImpl(
    private val repository: PostRepository
) : SendImagePostUseCase {
    override suspend fun invoke(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            repository.postImage(file)
        } else {
            throw PostException("File attempted to post does not exist")
        }
    }
}
