package com.hamdan.forzenbook.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hamdan.forzenbook.core.GlobalConstants.DAY_IN_MILLIS
import com.hamdan.forzenbook.data.entities.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun insert(userEntity: UserEntity)

    @Query(
        """
            SELECT * FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_ID} = :userId
        """
    )
    suspend fun getUser(userId: Int): List<UserEntity>

    @Query(
        """
            DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_ID} = :userId
        """
    )
    suspend fun deleteUser(userId: Int)

    @Query(
        """
            DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.ENTRY_DATE} <= (:currentTime - $DAY_IN_MILLIS)
        """
    )
    suspend fun deleteOldUsers(currentTime: Long)
}
