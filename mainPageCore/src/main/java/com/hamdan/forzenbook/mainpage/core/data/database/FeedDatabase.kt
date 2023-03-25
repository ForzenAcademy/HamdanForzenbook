package com.hamdan.forzenbook.mainpage.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FeedEntity::class], version = 1)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao

    companion object {
        const val NAME = "feed_db"
    }
}
