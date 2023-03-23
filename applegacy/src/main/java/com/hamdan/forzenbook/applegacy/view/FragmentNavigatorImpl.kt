package com.hamdan.forzenbook.applegacy.view

import androidx.fragment.app.FragmentManager
import com.hamdan.forzenbook.legacy.core.view.FragmentNavigator

// need an empty FragmentNavigatorImpl for dagger to be happy.
class FragmentNavigatorImpl : FragmentNavigator {
    override fun navigateToLogin(fragmentManager: FragmentManager) {
    }

    override fun navigateToCreateAccount(fragmentManager: FragmentManager) {
    }
}
