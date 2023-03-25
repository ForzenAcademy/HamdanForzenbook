package com.hamdan.forzenbook.mainpage.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hamdan.forzenbook.mainpage.core.data.repository.FeedRepositoryImpl.Companion.DAY_IN_MILLIS

@Dao
interface UserDao {

    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Query(
        """
            SELECT * FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_ID} = :userId
        """
    )
    suspend fun getUser(userId: String): List<UserEntity>

    @Query(
        """
            DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_ID} = :userId
        """
    )
    suspend fun deleteUser(userId: String)

    // the time value of 86400000 is 24 hours in milliseconds
    @Query(
        """
            DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.ENTRY_DATE} <= (:currentTime - $DAY_IN_MILLIS)
        """
    )
    suspend fun deleteOldUsers(currentTime: Long)
}
