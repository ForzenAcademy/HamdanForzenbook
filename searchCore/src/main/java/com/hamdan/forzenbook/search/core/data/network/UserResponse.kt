package com.hamdan.forzenbook.search.core.data.network

import com.google.gson.annotations.SerializedName
import com.hamdan.forzenbook.data.entities.UserEntity

data class UserResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("location") val location: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("avatarUrl") val avatarUrl: String? = null,
)

fun UserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        userIcon = this.avatarUrl,
        firstName = this.firstName,
        lastName = this.lastName,
        location = this.location,
        timestamp = System.currentTimeMillis(),
    )
}
