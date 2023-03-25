package com.hamdan.forzenbook.post.core.data.repository

interface PostRepository {
    suspend fun postText(token: String, message: String)

    suspend fun postImage(token: String)
}
