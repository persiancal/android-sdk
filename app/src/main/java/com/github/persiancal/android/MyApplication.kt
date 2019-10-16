package com.github.persiancal.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.github.persiancal.sdkremote.RemoteCalendarEvents

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        RemoteCalendarEvents.init(this)
    }
}