package com.hamdan.forzenbook.mainpage.core.data.repository

import com.hamdan.forzenbook.data.entities.Postable

interface FeedRepository {
    suspend fun getFeed(): List<Postable>
}
