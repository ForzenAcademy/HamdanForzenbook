package com.hamdan.forzenbook.createaccount.core.view

import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.LoginError

data class CreateUiComposeState(
    val errorId: Int? = null,
    val firstName: Entry = Entry("", LoginError.NameError.None),
    val lastName: Entry = Entry("", LoginError.NameError.None),
    val birthDay: Entry = Entry("", LoginError.BirthDateError.None),
    val email: Entry = Entry("", LoginError.EmailError.None),
    val location: Entry = Entry("", LoginError.LocationError.None),
    val isDateDialogOpen: Boolean = false,
    val isLoading: Boolean = false,
)
