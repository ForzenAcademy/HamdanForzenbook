package com.hamdan.forzenbook.legacy.fragment.app.view

import android.content.Context
import com.hamdan.forzenbook.legacy.core.view.Navigator

// Need an empty impl of Navigator for dagger to be happy
class NavigatorImpl : Navigator {
    override fun navigateToLogin(context: Context) {
    }

    override fun navigateToCreateAccount(context: Context) {
    }

    override fun navigateToPost(context: Context) {
    }
}
