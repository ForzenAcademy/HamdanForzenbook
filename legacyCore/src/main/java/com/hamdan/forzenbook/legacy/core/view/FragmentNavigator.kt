package com.hamdan.forzenbook.legacy.core.view

import androidx.fragment.app.FragmentManager

interface FragmentNavigator {
    fun navigateToLogin(fragmentManager: FragmentManager): Unit
    fun navigateToCreateAccount(fragmentManager: FragmentManager): Unit
}
