package com.pratik.streamvault

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class StreamVaultApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}