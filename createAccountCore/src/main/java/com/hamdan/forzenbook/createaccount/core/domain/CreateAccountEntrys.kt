package com.hamdan.forzenbook.createaccount.core.domain

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError

data class CreateAccountEntrys(
    val firstName: Entry = Entry("", EntryError.NameError.None),
    val lastName: Entry = Entry("", EntryError.NameError.None),
    val birthDay: Entry = Entry("", EntryError.BirthDateError.None),
    val email: Entry = Entry("", EntryError.EmailError.None),
    val location: Entry = Entry("", EntryError.LocationError.None)
)
