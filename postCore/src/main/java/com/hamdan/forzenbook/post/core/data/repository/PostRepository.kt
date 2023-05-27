package com.hamdan.forzenbook.post.core.data.repository

import java.io.File

interface PostRepository {
    suspend fun postText(message: String)

    suspend fun postImage(file: File)
}
