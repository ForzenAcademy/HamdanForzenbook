package com.hamdan.forzenbook.legacy.core.di

import com.hamdan.forzenbook.legacy.core.view.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object NavigatorModule {

    @Provides
    fun providesNavigator(): Navigator {
        // TODO In FA-104 implement this
        TODO() // return NavigatorImpl(loginContext:Context,createContext:Context)
    }

    private const val MODULE_NAME = "navigator"
}
