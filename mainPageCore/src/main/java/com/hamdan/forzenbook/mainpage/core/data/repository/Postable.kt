package com.hamdan.forzenbook.mainpage.core.data.repository

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.mainpage.core.data.database.FeedEntity
import com.hamdan.forzenbook.mainpage.core.data.database.UserEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Postable(
    val feedEntity: FeedEntity,
    val userEntity: UserEntity,
)

fun Postable.toPostData(): PostData {
    val dateString = this.feedEntity.created
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val outputFormat = DateTimeFormatter.ofPattern("MMMM d yyyy h:mm a")

    val dateTime = LocalDateTime.parse(dateString, inputFormat)
    val formattedDateTime = dateTime.format(outputFormat)

    return PostData(
        posterName = this.userEntity.name,
        posterLocation = this.userEntity.location,
        posterIcon = this.userEntity.userIcon,
        body = this.feedEntity.body,
        type = this.feedEntity.type,
        date = formattedDateTime,
    )
}
