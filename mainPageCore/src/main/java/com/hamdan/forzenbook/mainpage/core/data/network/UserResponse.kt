package com.hamdan.forzenbook.mainpage.core.data.network

import com.google.gson.annotations.SerializedName
import com.hamdan.forzenbook.data.entities.UserEntity

data class UserResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("email") val email: String,
    @SerializedName("birth_date") val birthDate: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("location") val location: String,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("about_me") val aboutMe: String,
    @SerializedName("created_at") val createdAt: String,
)

fun UserResponse.toUserEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        userImage = this.profileImage,
        firstName = this.firstName,
        lastName = this.lastName,
        location = this.location,
        timestamp = System.currentTimeMillis(),
        aboutMe = aboutMe,
    )
}
