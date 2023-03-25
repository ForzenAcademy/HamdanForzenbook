package com.hamdan.forzenbook.post.core.data.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface PostService {

    @FormUrlEncoded
    @POST(POST)
    suspend fun sendTextPost(
        @Header("token") token: String,
        @Field(TYPE) type: String,
        @Field(BODY) body: String,
    ): Response<Void>

//    // Todo implement Image Post when more details given
//    @Multipart
//    @POST(POST)
//    suspend fun sendImagePost(
//        @Header("token") token: String,
//        @Part(TYPE) type: RequestBody,
//        @Part image: MultipartBody.Part,
//    ): Response<Void>

    companion object {
        private const val TYPE = "post_type"
        private const val BODY = "body"
        private const val POST = "post_test/"
    }
}
