package com.hamdan.forzenbook.mainpage.core.data.repository

interface FeedRepository {
    suspend fun getFeed(nameFormat: String): List<Postable>
}
