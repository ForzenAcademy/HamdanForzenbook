package com.hamdan.forzenbook.post.core.data.repository

import java.io.File

interface PostRepository {
    suspend fun postText(token: String, message: String)

    suspend fun postImage(token: String, file: File)
}
