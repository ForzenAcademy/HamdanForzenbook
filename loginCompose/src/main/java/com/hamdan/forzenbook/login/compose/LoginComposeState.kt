package com.hamdan.forzenbook.login.compose

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError

data class LoginComposeState(
    val email: Entry = Entry("", LoginError.EmailError.None),
    val code: Entry = Entry("", LoginError.CodeError.None),
    val showInfoDialog: Boolean = false,
    val inputtingCode: Boolean = false,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
)
