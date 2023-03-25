package com.hamdan.forzenbook.search.core.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(SEARCH)
    suspend fun getPosts(
        @Query(USER_ID) id: Int? = null,
        @Query(QUERY) query: String? = null,
    ): Response<List<SearchResponse>>

    @GET(USER_GET)
    suspend fun getUser(
        @Query(USER_ID) id: Int,
    ): Response<UserResponse>

    companion object {
        private const val SEARCH = "search/posts"
        private const val USER_GET = "user"
        private const val USER_ID = "uid"
        private const val QUERY = "q"
    }
}
