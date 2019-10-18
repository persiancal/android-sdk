package com.github.persiancal.sdklocal

import android.content.Context
import com.github.persiancal.core.DatabaseHandler
import com.github.persiancal.core.base.EventsItem
import com.github.persiancal.core.base.EventsResponse
import com.github.persiancal.core.db.LocalGregorianEventsDb
import com.github.persiancal.core.db.LocalHijriEventsDb
import com.github.persiancal.core.db.LocalJalaliEventsDb
import com.github.persiancal.sdklocal.util.Constants
import com.google.gson.Gson

import java.io.InputStream


class LocalCalendarEvents {
    fun isHijriReady(): Boolean {
        return DatabaseHandler.getInstance().isLocalHijriReady()
    }

    fun getHijriEvents(dayOnMonth: Int, month: Int): MutableList<LocalHijriEventsDb>? {
        return DatabaseHandler.getInstance().getLocalHijriEvents(dayOnMonth, month)
    }

    fun isGregorianReady(): Boolean {
        return DatabaseHandler.getInstance().isLocalGregorianReady()
    }

    fun getGregorianEvents(dayOnMonth: Int, month: Int): MutableList<LocalGregorianEventsDb>? {
        return DatabaseHandler.getInstance().getLocalGregorianEvents(dayOnMonth, month)
    }

    fun isJalaliReady(): Boolean {
        return DatabaseHandler.getInstance().isLocalJalaliReady()
    }

    fun getJalaliEvents(dayOnMonth: Int, month: Int): MutableList<LocalJalaliEventsDb>? {
        return DatabaseHandler.getInstance().getLocalJalaliEvents(dayOnMonth, month)
    }

    companion object {
        private var instance: LocalCalendarEvents = LocalCalendarEvents()

        private var calendarTypeList = arrayListOf<CalendarType>()

        fun init(
            context: Context
        ) {
            DatabaseHandler.init(context)
            for (item in calendarTypeList) {
                when (item) {
                    CalendarType.JALALI -> {
                        if (!DatabaseHandler.getInstance().isLocalJalaliReady())
                            requestEvents(context, item, Constants.JALALI_ENDPOINT)
                    }
                    CalendarType.HIJRI -> {
                        if (!DatabaseHandler.getInstance().isLocalHijriReady())
                            requestEvents(context, item, Constants.HIJRI_ENDPOINT)
                    }
                    CalendarType.GREGORIAN -> {
                        if (!DatabaseHandler.getInstance().isLocalGregorianReady())
                            requestEvents(context, item, Constants.GREGORIAN_ENDPOINT)
                    }
                }
            }
        }

        private fun requestEvents(context: Context, calendarType: CalendarType, endpoint: Int) {
            val gson = Gson()
            val inputSteam: InputStream = context.resources.openRawResource(endpoint)
            val eventsResult: EventsResponse =
                gson.fromJson(inputSteam.reader(), EventsResponse::class.java)
            when (calendarType) {
                CalendarType.JALALI -> storeJalaliEvents(eventsResult.events)
                CalendarType.HIJRI -> storeHijriEvents(eventsResult.events)
                CalendarType.GREGORIAN -> storeGregorianEvents(eventsResult.events)
            }

        }


        private fun storeJalaliEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
                val jalaliEventsDb = LocalJalaliEventsDb(
                    0,
                    item.key,
                    item.calendar,
                    item.month,
                    item.sources,
                    item.year,
                    item.description!!.faIR,
                    item.title!!.faIR,
                    item.day,
                    holidayIran
                )
                DatabaseHandler.getInstance().putLocalJalaliEvents(jalaliEventsDb)
            }
        }

        private fun storeHijriEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
                val hijriEventsDb = LocalHijriEventsDb(
                    0,
                    item.key,
                    item.calendar,
                    item.month,
                    item.sources,
                    item.year,
                    item.description!!.faIR,
                    item.title!!.faIR,
                    item.day,
                    holidayIran
                )
                DatabaseHandler.getInstance().putLocalHijriEvents(hijriEventsDb)
            }
        }

        private fun storeGregorianEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
                val gregorianEventsDb = LocalGregorianEventsDb(
                    0,
                    item.key,
                    item.calendar,
                    item.month,
                    item.sources,
                    item.year,
                    item.description!!.faIR,
                    item.title!!.faIR,
                    item.day,
                    holidayIran
                )
                DatabaseHandler.getInstance().putLocalGregorianEvents(gregorianEventsDb)
            }
        }

        fun getInstance(): LocalCalendarEvents {
            return instance
        }

        fun addCalendar(calendarType: CalendarType): Companion {
            calendarTypeList.add(calendarType)
            return this
        }
    }
}