package com.hamdan.forzenbook.login.core.data.repository

sealed interface User {
    object NotLoggedIn : User
    object LoggedIn : User
}
