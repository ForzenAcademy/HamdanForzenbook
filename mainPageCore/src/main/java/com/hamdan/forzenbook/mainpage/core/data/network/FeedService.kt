package com.hamdan.forzenbook.mainpage.core.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET(FEED_GET)
    suspend fun getFeed(): Response<List<FeedResponse>>

    @GET(USER_GET)
    suspend fun getUser(
        @Query(USER_ID) id: Int,
    ): Response<UserResponse>

    companion object {
        private const val FEED_GET = "feed"
        private const val USER_GET = "user"
        private const val USER_ID = "id"
    }
}
