package com.hamdan.forzenbook.legacy.fragment.app.di

import com.hamdan.forzenbook.legacy.core.view.FragmentNavigator
import com.hamdan.forzenbook.legacy.core.view.Navigator
import com.hamdan.forzenbook.legacy.fragment.app.view.FragmentNavigatorImpl
import com.hamdan.forzenbook.legacy.fragment.app.view.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object FragmentNavigationModule {

    @Provides
    fun providesFragmentNavigator(): FragmentNavigator = FragmentNavigatorImpl()

    @Provides
    fun providesNavigatorImpl(): Navigator = NavigatorImpl()
    // this is an empty impl as the Fragments do not need a normal Navigator
}
