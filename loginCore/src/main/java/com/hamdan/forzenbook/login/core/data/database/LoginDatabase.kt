package com.hamdan.forzenbook.login.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LoginEntity::class], version = 1)
abstract class LoginDatabase : RoomDatabase() {
    abstract fun loginDao(): LoginDao

    companion object {
        const val NAME = "login_db"
    }
}
