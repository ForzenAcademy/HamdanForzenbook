package com.hamdan.forzenbook.data.entities

import com.hamdan.forzenbook.core.PostData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Postable(
    val postEntity: PostEntity,
    val userEntity: UserEntity,
)

fun Postable.toPostData(): PostData {
    val dateString = this.postEntity.created
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormat = DateTimeFormatter.ofPattern("MMMM d yyyy h:mm a")

    val dateTime = LocalDateTime.parse(dateString, inputFormat)
    val formattedDateTime = dateTime.format(outputFormat)

    return PostData(
        posterName = this.userEntity.name,
        posterLocation = this.userEntity.location,
        posterIcon = this.userEntity.userIcon,
        posterId = this.userEntity.userId,
        body = this.postEntity.body,
        type = this.postEntity.type,
        date = formattedDateTime,
    )
}
