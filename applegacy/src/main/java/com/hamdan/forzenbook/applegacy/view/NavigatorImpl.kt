package com.hamdan.forzenbook.applegacy.view

import android.content.Context
import android.content.Intent
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.legacy.createaccount.LegacyCreateAccountActivity
import com.hamdan.forzenbook.legacy.login.LegacyLoginActivity

class NavigatorImpl : Navigator {
    override fun navigateToCreateAccount(context: Context) {
        Intent(context, LegacyCreateAccountActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override fun navigateToLogin(context: Context) {
        Intent(context, LegacyLoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(this)
        }
    }
}
