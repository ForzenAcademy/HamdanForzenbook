package com.hamdan.forzenbook.post.core.data.repository

import com.hamdan.forzenbook.post.core.data.network.PostService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostRepositoryImpl(
    private val service: PostService
) : PostRepository {
    override suspend fun postText(token: String, message: String) {
        if (!service.sendTextPost(token, TEXT_TYPE, message).isSuccessful) {
            throw PostException("Error creating Post")
        }
    }

    override suspend fun postImage(token: String, file: File) {
        val requestBodyType = RequestBody.create(MediaType.parse("multipart/form-data"), IMAGE_TYPE)
        val requestBodyFile = RequestBody.create(MediaType.parse("image/*"), file)
        val response = service.sendImagePost(
            token,
            requestBodyType,
            MultipartBody.Part.createFormData("fileToUpload", file.name, requestBodyFile)
        )
        if (!response.isSuccessful) {
            throw PostException("Error creating Post")
        }
    }

    companion object {
        private const val TEXT_TYPE = "text"
        private const val IMAGE_TYPE = "image"
    }
}

class PostException(message: String) : Exception(message)
