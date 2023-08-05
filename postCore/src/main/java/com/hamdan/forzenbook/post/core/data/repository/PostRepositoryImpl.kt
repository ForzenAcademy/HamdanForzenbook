package com.hamdan.forzenbook.post.core.data.repository

import android.content.Context
import com.hamdan.forzenbook.core.InvalidTokenException
import com.hamdan.forzenbook.core.PostException
import com.hamdan.forzenbook.core.getToken
import com.hamdan.forzenbook.core.removeToken
import com.hamdan.forzenbook.post.core.data.network.PostService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class PostRepositoryImpl(
    private val service: PostService,
    private val context: Context,
) : PostRepository {
    override suspend fun postText(message: String) {
        val token = getToken(context)
        if (token.isNullOrEmpty()){
            removeToken(context)
            throw InvalidTokenException()
        }

        val response = service.sendTextPost(token, TEXT_TYPE, message)
        if (!response.isSuccessful) {
            if (response.code() == UNAUTHORIZED) {
                removeToken(context)
                throw (InvalidTokenException())
            } else {
                throw PostException("Error creating Post")
            }
        }
    }

    override suspend fun postImage(file: File) {
        val token = getToken(context)
        if (token.isNullOrEmpty()){
            removeToken(context)
            throw InvalidTokenException()
        }

        val requestBodyType =
            IMAGE_TYPE.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestBodyFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val response = service.sendImagePost(
            token,
            requestBodyType,
            MultipartBody.Part.createFormData("fileToUpload", file.name, requestBodyFile)
        )
        if (!response.isSuccessful) {
            if (response.code() == UNAUTHORIZED) {
                removeToken(context)
                throw (InvalidTokenException())
            } else {
                throw PostException("Error creating Post")
            }
        }
    }

    companion object {
        private const val UNAUTHORIZED = 401
        private const val TEXT_TYPE = "text"
        private const val IMAGE_TYPE = "image"
    }
}
