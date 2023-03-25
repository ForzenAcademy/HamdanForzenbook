package com.hamdan.forzenbook.post.core.domain

import com.hamdan.forzenbook.post.core.data.repository.PostException
import com.hamdan.forzenbook.post.core.data.repository.PostRepository
import java.io.File

class SendImagePostUseCaseImpl(
    private val repository: PostRepository
) : SendImagePostUseCase {
    override suspend fun invoke(token: String, filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            repository.postImage(token, file)
        } else {
            throw PostException("File attempted to post does not exist")
        }
    }
}
