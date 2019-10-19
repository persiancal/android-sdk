package com.github.persiancal.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.github.persiancal.core.CalendarType
import com.github.persiancal.sdklocal.LocalCalendarEvents
import com.github.persiancal.sdkremote.RemoteCalendarEvents

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        RemoteCalendarEvents
            .addCalendar(CalendarType.JALALI)
            .addCalendar(CalendarType.HIJRI)
            .addCalendar(CalendarType.GREGORIAN)
            .init(this)

        LocalCalendarEvents
            .addCalendar(CalendarType.JALALI)
            .addCalendar(CalendarType.HIJRI)
            .addCalendar(CalendarType.GREGORIAN)
            .init(this)
    }
}