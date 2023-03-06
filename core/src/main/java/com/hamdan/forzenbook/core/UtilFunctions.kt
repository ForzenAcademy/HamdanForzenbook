package com.hamdan.forzenbook.core

import android.content.Context
import com.hamdan.forzenbook.ui.core.R
import java.util.regex.Pattern

fun validateEmail(email: String): Boolean {
    val regex =
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    val pattern = Pattern.compile(regex)
    return pattern.matcher(email).matches()
}

fun stringDate(month: Int, day: Int, year: Int, context: Context): String {
    val date =
        context.getString(R.string.create_account_date, month, day, year).split("-")
    return date.joinToString("-") { it.leftPad() }
}

fun String.leftPad(): String {
    return if (this.length < 2) {
        "0$this"
    } else this
}
