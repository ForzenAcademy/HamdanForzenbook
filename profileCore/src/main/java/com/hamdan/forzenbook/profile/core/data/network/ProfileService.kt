package com.hamdan.forzenbook.profile.core.data.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface ProfileService {

    @GET(PROFILE_GET)
    suspend fun getProfile(
        @Header(TOKEN) token: String,
        @Query(USER_ID) id: Int,
    ): Response<ProfileResponse>

    @GET(PROFILE_GET)
    suspend fun getUsersProfile(
        @Header(TOKEN) token: String,
    ): Response<ProfileResponse>

    @Multipart
    @POST(PROFILE_POST)
    suspend fun updateImage(
        @Header(TOKEN) token: String,
        @Part(USER_ID) id: RequestBody,
        @Part profileImage: MultipartBody.Part,
    ): Response<ImageResponse>

    @PUT(PROFILE_PUT)
    suspend fun updateAboutMe(
        @Header(TOKEN) token: String,
        @Body params: RequestBody   // this is how we will send a raw json as the body
    ): Response<Void>

    companion object {
        private const val TOKEN = "token"
        private const val PROFILE_GET = "profile"
        private const val PROFILE_POST = "profileimage/"
        private const val PROFILE_PUT = "profile/"
        private const val USER_ID = "id"
    }
}