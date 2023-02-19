package com.hamdan.forzenbook.legacy.core.view

import android.content.Context

class NavigatorImpl(
    val legacyLoginContext: Context,
    val legacyCreateAccountContext: Context,
) : Navigator {
    // Todo implement both when FA-100 and 102 are implemented
    override fun navigateToLogin() {
// may want to change this from implicit intent incase we want to fill in email other options are possible too

//        Intent(legacyLoginContext,LegacyCreateAccountActivity::class.java).apply {
//            startActivity(legacyCreateAccountContext)
//        }
    }

    override fun navigateToCreateAccount() {
//        Intent(legacyCreateAccountContext,LegacyLoginActivity::class.java).apply {
//            startActivity(legacyLoginContext)
//        }
    }
}
