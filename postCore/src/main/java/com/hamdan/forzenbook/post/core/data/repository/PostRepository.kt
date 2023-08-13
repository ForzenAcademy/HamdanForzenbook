package com.hamdan.forzenbook.post.core.data.repository

import java.io.File

interface PostRepository {
    suspend fun sendTextPost(message: String)

    suspend fun sendTextPost(file: File)
}
