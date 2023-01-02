package com.example.forzenbook.data.repository

import com.example.forzenbook.data.database.LoginEntity

data class LoginData(
    val token: String?
) {
}

fun LoginEntity.toLoginData(): LoginData? {
    return LoginData(
        token = this.token
    )
}