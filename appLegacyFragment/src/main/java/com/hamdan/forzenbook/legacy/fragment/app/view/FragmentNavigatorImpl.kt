package com.hamdan.forzenbook.legacy.fragment.app.view

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.hamdan.forzenbook.legacy.core.view.FragmentNavigator
import com.hamdan.forzenbook.legacy.createaccount.fragment.LegacyCreateAccountFragment
import com.hamdan.forzenbook.legacy.fragment.app.R
import com.hamdan.forzenbook.legacy.login.fragment.LegacyLoginFragment

class FragmentNavigatorImpl : FragmentNavigator {
    override fun navigateToLogin(fragmentManager: FragmentManager) {
        fragmentManager.popBackStack(CREATE_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, LegacyLoginFragment::class.java, null)
        }
    }

    override fun navigateToCreateAccount(fragmentManager: FragmentManager) {
        fragmentManager.commit {
            addToBackStack(CREATE_TAG)
            setReorderingAllowed(true)
            replace(R.id.fragment_container_view, LegacyCreateAccountFragment::class.java, null)
        }
    }

    companion object {
        private const val CREATE_TAG = "create"
    }
}
