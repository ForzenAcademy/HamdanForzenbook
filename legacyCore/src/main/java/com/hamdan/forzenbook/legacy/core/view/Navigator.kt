package com.hamdan.forzenbook.legacy.core.view

import android.content.Context

interface Navigator {
    fun navigateToLogin(context: Context): Unit
    fun navigateToCreateAccount(context: Context): Unit
}
