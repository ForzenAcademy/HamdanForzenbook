package com.hamdan.forzenbook.legacy.core.view

import android.content.Context

interface Navigator {
    fun navigateToLogin(legacyLoginContext: Context): Unit
    fun navigateToCreateAccount(legacyCreateAccountContext: Context): Unit
}
