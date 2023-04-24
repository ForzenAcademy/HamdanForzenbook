package com.hamdan.forzenbook.mainpage.core.data.network

import com.google.gson.annotations.SerializedName
import com.hamdan.forzenbook.data.entities.PostEntity

data class FeedResponse(
    @SerializedName("post_id") val postId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("body") val body: String,
    @SerializedName("post_type") val type: String,
    @SerializedName("created_at") val created: String,
)

fun FeedResponse.toPostEntity(): PostEntity {
    return PostEntity(
        postId = this.postId,
        userId = this.userId,
        body = this.body,
        type = this.type,
        created = this.created,
        timestamp = System.currentTimeMillis()
    )
}
