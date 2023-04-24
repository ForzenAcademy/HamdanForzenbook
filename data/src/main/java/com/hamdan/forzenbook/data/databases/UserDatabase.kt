package com.hamdan.forzenbook.data.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hamdan.forzenbook.data.daos.UserDao
import com.hamdan.forzenbook.data.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val NAME = "user_db"
    }
}
