package com.hamdan.forzenbook.login.core.domain.usecase

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError

data class LoginEntrys(
    val email: Entry = Entry("", EntryError.EmailError.None),
    val code: Entry = Entry("", EntryError.CodeError.None),
)
