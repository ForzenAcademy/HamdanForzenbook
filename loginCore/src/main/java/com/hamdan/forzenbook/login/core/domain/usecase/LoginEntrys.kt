package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError

data class LoginEntrys(
    val email: Entry = Entry("", LoginError.EmailError.None),
    val code: Entry = Entry("", LoginError.CodeError.None),
)
