package com.github.persiancal.sdklocal

import android.content.Context
import com.github.persiancal.sdklocal.model.base.EventsItem
import com.github.persiancal.sdklocal.util.Constants


class LocalCalendarEvents {

    companion object {
        private var instance: LocalCalendarEvents = LocalCalendarEvents()
        private var calendarTypeList = arrayListOf<CalendarType>()

        fun init(
            context: Context
        ) {
            for (item in calendarTypeList) {
                when (item) {
                    CalendarType.JALALI -> {

                        requestEvents(item, Constants.JALALI_ENDPOINT)

                    }
                    CalendarType.HIJRI -> {

                        requestEvents(item, Constants.HIJRI_ENDPOINT)


                    }
                    CalendarType.GREGORIAN -> {
                        requestEvents(item, Constants.GREGORIAN_ENDPOINT)

                    }
                }
            }
        }

        private fun requestEvents(calendarType: CalendarType, endpoint: String) {

        }


        private fun storeJalaliEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
            }
        }

        private fun storeHijriEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
            }
        }

        private fun storeGregorianEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
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