package com.hamdan.forzenbook.data.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hamdan.forzenbook.data.daos.FeedDao
import com.hamdan.forzenbook.data.entities.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    companion object {
        const val NAME = "feed_db"
    }
}
