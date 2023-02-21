package com.hamdan.forzenbook.legacy.core.view

import android.content.Context

class NavigatorImpl() : Navigator {
    // Todo implement both when FA-100 and 102 are implemented
    override fun navigateToLogin(legacyLoginContext: Context) {
// may want to change this from explicit intent to implict incase we want to fill in email other options are possible too

//        Intent(legacyLoginContext,LegacyCreateAccountActivity::class.java).apply { intent->
//            startActivity(intent)
//        }
    }

    override fun navigateToCreateAccount(legacyCreateAccountContext: Context) {
//        Intent(legacyCreateAccountContext,LegacyLoginActivity::class.java).apply { intent->
//            startActivity(intent)
//        }
    }
}
