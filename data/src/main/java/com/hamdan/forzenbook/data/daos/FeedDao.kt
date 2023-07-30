package com.hamdan.forzenbook.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.data.entities.PostEntity

@Dao
interface FeedDao {

    @Insert
    suspend fun insert(postEntity: PostEntity)

    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME}
        """
    )
    suspend fun getCachedFeed(): List<PostEntity>

    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} = :postId
        """
    )
    suspend fun getSpecificPostId(postId: Int): List<PostEntity>

    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} < :postId ORDER BY ${PostEntity.POST_ID} ASC LIMIT :size
        """
    )
    suspend fun getDownPagedPosts(postId: Int, size: Int): List<PostEntity>

    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} > :postId ORDER BY ${PostEntity.POST_ID} DESC LIMIT :size
        """
    )
    suspend fun getUpPagedPosts(postId: Int, size: Int): List<PostEntity>

    @Query(
        """
            DELETE FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} = :postId 
        """
    )
    suspend fun deleteSpecificPost(postId: Int)

    @Query(
        """
            DELETE FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.ENTRY_DATE} <= (:currentTime - ${GlobalConstants.DAY_IN_MILLIS})
        """
    )
    suspend fun deleteOldPosts(currentTime: Long)
}
