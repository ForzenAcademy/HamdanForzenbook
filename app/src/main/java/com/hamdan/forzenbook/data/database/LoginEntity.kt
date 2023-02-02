package com.hamdan.forzenbook.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hamdan.forzenbook.data.database.LoginEntity.Companion.TABLE_NAME

// TODO when creating homepage login with token, may need to remove the email portion from this as only a token may be needed
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
        const val DATABASE_ID = "login_database_id" // TABLE_NAME + "_database_id"
        const val TOKEN = "login_token" // TABLE_NAME + "_token"
        const val EMAIL = "login_email"
    }
}
