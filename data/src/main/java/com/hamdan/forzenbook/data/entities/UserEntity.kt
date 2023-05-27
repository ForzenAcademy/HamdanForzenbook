package com.hamdan.forzenbook.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hamdan.forzenbook.data.entities.UserEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserEntity(
    @ColumnInfo(name = USER_ID) val userId: Int,
    @ColumnInfo(name = USER_ICON) val userIcon: String?,
    @ColumnInfo(name = USER_FIRST_NAME) val firstName: String,
    @ColumnInfo(name = USER_LAST_NAME) val lastName: String,
    @ColumnInfo(name = LOCATION) val location: String,
    @ColumnInfo(name = ENTRY_DATE) val timestamp: Long,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DATABASE_ID)
    var userDbId: Int = 0

    companion object {
        const val TABLE_NAME = "user"
        const val DATABASE_ID = "user_database_id"
        const val USER_ID = "user_user_id"
        const val USER_ICON = "user_icon"
        const val USER_FIRST_NAME = "user_first_name"
        const val USER_LAST_NAME = "user_last_name"
        const val LOCATION = "user_location"
        const val ENTRY_DATE = "user_timestamp"
    }
}
