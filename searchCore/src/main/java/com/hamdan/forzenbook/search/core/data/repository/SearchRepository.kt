package com.hamdan.forzenbook.search.core.data.repository

import com.hamdan.forzenbook.data.entities.Postable

interface SearchRepository {
    suspend fun getPostByUserId(id: Int): List<Postable>
    suspend fun getPostByQuery(query: String): List<Postable>
    suspend fun searchPostByUserId(id: Int)
    suspend fun searchPostByQuery(query: String)
}
