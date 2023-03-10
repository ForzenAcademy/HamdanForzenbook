package com.hamdan.forzenbook.login.core.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hamdan.forzenbook.login.core.data.database.LoginEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class LoginEntity(
    @ColumnInfo(name = TOKEN) val token: String,
    @ColumnInfo(name = EMAIL) val email: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DATABASE_ID)
    var loginId: Int = 0

    companion object {
        const val TABLE_NAME = "login"
        const val DATABASE_ID = "login_database_id"
        const val TOKEN = "login_token"
        const val EMAIL = "login_email"
    }
}
