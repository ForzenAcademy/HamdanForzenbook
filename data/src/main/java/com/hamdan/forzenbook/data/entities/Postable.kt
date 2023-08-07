package com.hamdan.forzenbook.data.entities

import com.hamdan.forzenbook.core.PostData
import com.hamdan.forzenbook.core.convertDate

data class Postable(
    val postEntity: PostEntity,
    val userEntity: UserEntity,
)

/**
 * format user data and post info into one unified post data
 */
fun Postable.toPostData() = PostData(
    posterFirstName = this.userEntity.firstName,
    posterLastName = this.userEntity.lastName,
    posterLocation = this.userEntity.location,
    posterIcon = this.userEntity.userImage,
    posterId = this.userEntity.userId,
    body = this.postEntity.body,
    type = this.postEntity.type,
    date = convertDate(this.postEntity.created),
    postId = this.postEntity.postId,
)
