package com.hamdan.forzenbook.search.core.data.network

import com.google.gson.annotations.SerializedName
import com.hamdan.forzenbook.data.entities.PostEntity

data class SearchResponse(
    @SerializedName("post_id") val postId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("body") val body: String,
    @SerializedName("post_type") val type: String,
    @SerializedName("created_at") val created: String,
)

fun SearchResponse.toFeedEntity(): PostEntity {
    return PostEntity(
        postId = this.postId,
        userId = this.userId,
        body = this.body,
        type = this.type,
        created = this.created,
        timestamp = System.currentTimeMillis()
    )
}
