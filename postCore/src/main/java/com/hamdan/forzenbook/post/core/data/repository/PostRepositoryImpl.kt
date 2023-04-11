package com.hamdan.forzenbook.post.core.data.repository

import com.hamdan.forzenbook.post.core.data.network.PostService

class PostRepositoryImpl(
    private val service: PostService
) : PostRepository {
    override suspend fun postText(token: String, message: String) {
        if (!service.sendTextPost(token, TEXT_TYPE, message).isSuccessful) {
            throw PostException("Error creating Post")
        }
    }

    override suspend fun postImage(token: String) {
        // Todo implement when further info given
    }

    companion object {
        private const val TEXT_TYPE = "text"
        private const val IMAGE_TYPE = "image" // Todo update these when more details given
    }
}

class PostException(message: String) : Exception(message)
