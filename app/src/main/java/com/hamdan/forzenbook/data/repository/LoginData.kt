package com.hamdan.forzenbook.data.repository

import com.hamdan.forzenbook.data.database.LoginEntity

data class LoginData(
    val token: String?
)

fun LoginEntity.toLoginData(): LoginData? {
    return LoginData(
        token = this.token
    )
}
