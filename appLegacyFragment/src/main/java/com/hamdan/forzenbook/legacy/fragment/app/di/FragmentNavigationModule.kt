package com.hamdan.forzenbook.legacy.fragment.app.di

import android.content.Context
import com.hamdan.forzenbook.legacy.core.view.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object FragmentNavigationModule {

    @Provides
    fun providesFragmentNavigator(): Navigator = NavigatorImpl()
}

// Todo remove this impl and create an actual one FA-126
// Todo the impl should perform a transaction for the fragments

class NavigatorImpl() : Navigator {
    override fun navigateToLogin(context: Context) {
    }

    override fun navigateToCreateAccount(context: Context) {
    }
}
