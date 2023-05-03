package com.hamdan.forzenbook.post.core.data.repository

import com.hamdan.forzenbook.core.NetworkRetrievalException
import com.hamdan.forzenbook.core.PostException
import com.hamdan.forzenbook.post.core.data.network.PostService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class PostRepositoryImpl(
    private val service: PostService
) : PostRepository {
    override suspend fun postText(token: String, message: String) {
        val response = service.sendTextPost(token, TEXT_TYPE, message)
        if (!response.isSuccessful) {
            if(response.code() == 401){
                throw(NetworkRetrievalException())
            }else{
                throw PostException("Error creating Post")
            }
        }
    }

    override suspend fun postImage(token: String, file: File) {
        // Todo update this so its not deprecated
        val requestBodyType =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), IMAGE_TYPE)
        val requestBodyFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val response = service.sendImagePost(
            token,
            requestBodyType,
            MultipartBody.Part.createFormData("fileToUpload", file.name, requestBodyFile)
        )
        if (!response.isSuccessful) {
            if(response.code() == 401){
                throw(NetworkRetrievalException())
            }else{
                throw PostException("Error creating Post")
            }
        }
    }

    companion object {
        private const val TEXT_TYPE = "text"
        private const val IMAGE_TYPE = "image"
    }
}
