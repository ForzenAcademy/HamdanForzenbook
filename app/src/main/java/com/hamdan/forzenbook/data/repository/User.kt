package com.hamdan.forzenbook.data.repository

sealed interface User {
    object NotLoggedIn : User
    object LoggedIn : User
}
