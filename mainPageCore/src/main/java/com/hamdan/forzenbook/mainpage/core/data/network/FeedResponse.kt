package com.hamdan.forzenbook.mainpage.core.data.network

import com.google.gson.annotations.SerializedName
import com.hamdan.forzenbook.mainpage.core.data.database.FeedEntity

data class FeedResponse(
    @SerializedName("post_id") val postId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("body") val body: String,
    @SerializedName("post_type") val type: String,
    @SerializedName("created_at") val created: String,
)

fun FeedResponse.toFeedEntity(): FeedEntity {
    return FeedEntity(
        postId = this.postId.toInt(),
        userId = this.userId.toInt(),
        body = this.body,
        type = this.type,
        created = this.created,
        timestamp = System.currentTimeMillis()
    )
}
