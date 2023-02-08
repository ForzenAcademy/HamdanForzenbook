package com.hamdan.forzenbook.createaccount.core.domain

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError

data class CreateAccountEntrys(
    val firstName: Entry = Entry("", LoginError.NameError.Length),
    val lastName: Entry = Entry("", LoginError.NameError.Length),
    val birthDay: Entry = Entry("", LoginError.BirthDateError.TooYoung),
    val email: Entry = Entry("", LoginError.EmailError.Length),
    val location: Entry = Entry("", LoginError.LocationError.Length)
)
