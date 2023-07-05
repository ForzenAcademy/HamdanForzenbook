package com.hamdan.forzenbook.profile.core.data.repository

import com.hamdan.forzenbook.data.PagingDirection
import com.hamdan.forzenbook.data.entities.Postable
import com.hamdan.forzenbook.profile.core.data.network.ImageResponse
import java.io.File

interface ProfileRepository {
    suspend fun getProfile(userId: Int?): ProfileInfo
    suspend fun getPagedPosts(
        postId: Int,
        userId: Int,
        pagingDirection: PagingDirection
    ): List<Postable>

    suspend fun updateAboutMe(userId: Int, text: String)
    suspend fun updateIcon(userId: Int, file: File): ImageResponse
}