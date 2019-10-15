package com.github.persiancal.android

import android.app.Application
import com.github.persiancal.androidsdk.CalendarEvents

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CalendarEvents.init(this)
    }
}