package com.hamdan.forzenbook.applegacy.view

import android.content.Context
import android.content.Intent
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.legacy.createaccount.LegacyCreateAccountActivity
import com.hamdan.forzenbook.legacy.login.LegacyLoginActivity
import com.hamdan.forzenbook.legacy.mainpage.view.LegacyMainPageActivity
import com.hamdan.forzenbook.legacy.post.LegacyPostActivity

class NavigatorImpl : Navigator {
    override fun navigateToCreateAccount(context: Context) {
        Intent(context, LegacyCreateAccountActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override fun navigateToPost(context: Context) {
        Intent(context, LegacyPostActivity::class.java).apply {
            context.startActivity(this)
        }
    }

    override fun kickToLogin(context: Context) {
        Intent(context, LegacyLoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }
    }

    override fun navigateToLogin(context: Context) {
        Intent(context, LegacyLoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(this)
        }
    }

    // whenever navigating to feed generally want to clear top anyway so we can use this for all cases of feed
    override fun navigateToFeed(context: Context) {
        Intent(context, LegacyMainPageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(this)
        }
    }
}
