package com.hamdan.forzenbook.search.core.data.repository

import com.hamdan.forzenbook.data.entities.Postable

interface SearchRepository {
    suspend fun getPostByUserId(id: Int, token: String): List<Postable>
    suspend fun getPostByQuery(query: String, token: String): List<Postable>
    suspend fun searchPostByUserId(id: Int, token: String)
    suspend fun searchPostByQuery(query: String, token: String)
}
