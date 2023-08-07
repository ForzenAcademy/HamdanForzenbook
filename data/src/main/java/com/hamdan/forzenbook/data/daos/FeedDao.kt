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

    /**
     * gets a specified post
     */
    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} = :postId
        """
    )
    suspend fun getSpecificPostId(postId: Int): List<PostEntity>

    /**
     * gets posts with ids smaller than the post id, gets an amount limited to the size specified
     */
    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} < :postId AND ${PostEntity.USER_ID} = :userId ORDER BY ${PostEntity.POST_ID} ASC LIMIT :size
        """
    )
    suspend fun getDownPagedPosts(postId: Int, userId: Int, size: Int): List<PostEntity>

    /**
     * gets posts with ids larger than the post id, gets an amount limited to the size specified
     */
    @Query(
        """
            SELECT * FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} > :postId AND ${PostEntity.USER_ID} = :userId ORDER BY ${PostEntity.POST_ID} DESC LIMIT :size
        """
    )
    suspend fun getUpPagedPosts(postId: Int, userId: Int, size: Int): List<PostEntity>

    /**
     * delete a specified post
     */
    @Query(
        """
            DELETE FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.POST_ID} = :postId 
        """
    )
    suspend fun deleteSpecificPost(postId: Int)

    /**
     * delete all posts older than the set interval
     */
    @Query(
        """
            DELETE FROM ${PostEntity.TABLE_NAME} WHERE ${PostEntity.ENTRY_DATE} <= (:currentTime - ${GlobalConstants.DAY_IN_MILLIS})
        """
    )
    suspend fun deleteOldPosts(currentTime: Long)
}
