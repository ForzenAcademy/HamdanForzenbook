package com.hamdan.forzenbook.mainpage.core.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hamdan.forzenbook.mainpage.core.data.database.FeedEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class FeedEntity(
    @ColumnInfo(name = POST_ID) val postId: Int,
    @ColumnInfo(name = USER_ID) val userId: Int,
    @ColumnInfo(name = BODY) val body: String,
    @ColumnInfo(name = POST_TYPE) val type: String,
    @ColumnInfo(name = CREATED_AT) val created: String,
    @ColumnInfo(name = ENTRY_DATE) val timestamp: Long,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DATABASE_ID)
    var feedId: Int = 0

    companion object {
        const val TABLE_NAME = "feed"
        const val DATABASE_ID = "feed_database_id"
        const val POST_ID = "feed_post_id"
        const val USER_ID = "feed_user_id"
        const val BODY = "feed_body"
        const val POST_TYPE = "feed_type"
        const val CREATED_AT = "feed_created_at"
        const val ENTRY_DATE = "feed_timestamp"
    }
}
