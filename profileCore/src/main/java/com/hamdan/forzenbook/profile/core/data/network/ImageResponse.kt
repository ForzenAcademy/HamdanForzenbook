package com.hamdan.forzenbook.profile.core.data.network

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("image") val image: String,
)