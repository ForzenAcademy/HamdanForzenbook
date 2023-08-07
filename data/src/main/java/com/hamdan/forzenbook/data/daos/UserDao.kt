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

    /**
     * Get a specific user
     */
    @Query(
        """
            SELECT * FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_ID} = :userId
        """
    )
    suspend fun getUser(userId: Int): List<UserEntity>

    /**
     * Delete a specific user
     */
    @Query(
        """
            DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.USER_ID} = :userId
        """
    )
    suspend fun deleteUser(userId: Int)

    /**
     * Delete all users older than the interval
     */
    @Query(
        """
            DELETE FROM ${UserEntity.TABLE_NAME} WHERE ${UserEntity.ENTRY_DATE} <= (:currentTime - $DAY_IN_MILLIS)
        """
    )
    suspend fun deleteOldUsers(currentTime: Long)

    /**
     * update a users profile image path
     */
    @Query(
        """
             UPDATE ${UserEntity.TABLE_NAME} SET  ${UserEntity.USER_PROFILE_IMAGE} = :newImagePath WHERE(${UserEntity.USER_ID} = :userId)
        """
    )
    suspend fun updateUserImage(newImagePath: String, userId: Int)
}
