package com.github.persiancal.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.github.persiancal.androidsdk.CalendarEvents

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        CalendarEvents.init(this)
    }
}