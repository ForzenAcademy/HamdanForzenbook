package com.hamdan.forzenbook.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * This is required for Dagger to inject
 */
@HiltAndroidApp
class ForzenbookApplication : Application()
