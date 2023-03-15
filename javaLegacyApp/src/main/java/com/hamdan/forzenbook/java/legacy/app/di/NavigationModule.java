package com.hamdan.forzenbook.java.legacy.app.di;

import com.hamdan.forzenbook.java.core.NavigatorJava;
import com.hamdan.forzenbook.java.legacy.app.view.NavigatorJavaImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@InstallIn(ViewModelComponent.class)
@Module
public class NavigationModule {

    @Provides
    public NavigatorJava providesNavigatorJavaImpl() {
        return new NavigatorJavaImpl();
    }
}
