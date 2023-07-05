package com.hamdan.forzenbook.profile.core.data.repository

import com.hamdan.forzenbook.data.entities.Postable

data class ProfileInfo(
    val firstName: String,
    val lastName:String,
    val userId: Int,
    val isOwner: Boolean,
    val postSet: List<Postable>,
    val userIconPath: String,
    val dateJoined: String,
    val aboutUser: String,
)