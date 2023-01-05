package com.hamdan.forzenbook.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginDao {

    @Insert
    suspend fun insert(loginEntity: LoginEntity)

    @Query(
        """
            SELECT * FROM ${LoginEntity.TABLE_NAME}
            LIMIT 1
        """
    )
    suspend fun getToken(): LoginEntity?


}