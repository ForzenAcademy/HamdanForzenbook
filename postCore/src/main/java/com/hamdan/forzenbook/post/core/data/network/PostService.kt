package com.hamdan.forzenbook.post.core.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PostService {

    @FormUrlEncoded
    @POST(POST)
    suspend fun sendTextPost(
        @Header(TOKEN) token: String,
        @Field(TYPE) type: String,
        @Field(BODY) body: String,
    ): Response<Void>

    @Multipart
    @POST(POST)
    suspend fun sendImagePost(
        @Header(TOKEN) token: String,
        @Part(TYPE) type: RequestBody,
        @Part image: MultipartBody.Part,
    ): Response<Void>

    companion object {
        private const val TOKEN = "token"
        private const val TYPE = "postType"
        private const val BODY = "body"
        private const val POST = "post/"
    }
}
