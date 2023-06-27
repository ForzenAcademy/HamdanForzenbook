package com.hamdan.forzenbook.legacy.core.view

import android.content.Context

interface Navigator {
    fun navigateToLogin(context: Context)
    fun navigateToFeed(context: Context)
    fun navigateToCreateAccount(context: Context)
    fun navigateToPost(context: Context)
    fun kickToLogin(context: Context)
}
