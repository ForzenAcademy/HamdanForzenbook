package com.hamdan.forzenbook.applegacy.di

import com.hamdan.forzenbook.applegacy.view.NavigatorImpl
import com.hamdan.forzenbook.legacy.core.view.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object NavigationModule {
    @Provides
    fun providesNavigationImpl(): Navigator = NavigatorImpl()
}
