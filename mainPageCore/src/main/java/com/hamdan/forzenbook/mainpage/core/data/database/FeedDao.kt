package com.hamdan.forzenbook.mainpage.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hamdan.forzenbook.mainpage.core.data.repository.FeedRepositoryImpl.Companion.DAY_IN_MILLIS

@Dao
interface FeedDao {

    @Insert
    suspend fun insert(feedEntity: FeedEntity)

    @Query(
        """
            SELECT * FROM ${FeedEntity.TABLE_NAME}
        """
    )
    suspend fun getCachedFeed(): List<FeedEntity>

    @Query(
        """
            SELECT * FROM ${FeedEntity.TABLE_NAME} WHERE ${FeedEntity.POST_ID} = :postId
        """
    )
    suspend fun getSpecificPostId(postId: String): List<FeedEntity>

    @Query(
        """
            DELETE FROM ${FeedEntity.TABLE_NAME} WHERE ${FeedEntity.ENTRY_DATE} <= (:currentTime - $DAY_IN_MILLIS)
        """
    )
    suspend fun deleteOldPosts(currentTime: Long)
}
