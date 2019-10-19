package com.github.persiancal.sdkremote

import android.content.Context
import com.github.persiancal.core.CalendarType
import com.github.persiancal.core.DatabaseHandler
import com.github.persiancal.core.db.RemoteGregorianEventsDb
import com.github.persiancal.core.db.RemoteHijriEventsDb
import com.github.persiancal.core.db.RemoteJalaliEventsDb
import com.github.persiancal.sdkremote.service.ApiService
import com.github.persiancal.sdkremote.util.ApiClient
import com.github.persiancal.sdkremote.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RemoteCalendarEvents {
    fun isHijriReady(): Boolean {
        return DatabaseHandler.getInstance().isRemoteHijriReady()
    }

    fun getHijriEvents(dayOnMonth: Int, month: Int): MutableList<RemoteHijriEventsDb>? {
        return DatabaseHandler.getInstance().getRemoteHijriEvents(dayOnMonth, month)
    }

    fun isGregorianReady(): Boolean {
        return DatabaseHandler.getInstance().isRemoteGregorianReady()
    }

    fun getGregorianEvents(dayOnMonth: Int, month: Int): MutableList<RemoteGregorianEventsDb>? {
        return DatabaseHandler.getInstance().getRemoteGregorianEvents(dayOnMonth, month)
    }

    fun isJalaliReady(): Boolean {
        return DatabaseHandler.getInstance().isRemoteJalaliReady()
    }

    fun getJalaliEvents(dayOnMonth: Int, month: Int): MutableList<RemoteJalaliEventsDb>? {
        return DatabaseHandler.getInstance().getRemoteJalaliEvents(dayOnMonth, month)
    }

    companion object {
        private var instance: RemoteCalendarEvents = RemoteCalendarEvents()
        private lateinit var apiService: ApiService

        private var calendarTypeList = arrayListOf<CalendarType>()

        fun init(
            context: Context
        ) {
            DatabaseHandler.init(context)
            for (item in calendarTypeList) {
                when (item) {
                    CalendarType.JALALI -> {
                        if (!DatabaseHandler.getInstance().isRemoteJalaliReady()) {
                            requestEvents(item, Constants.JALALI_ENDPOINT)
                        }
                    }
                    CalendarType.HIJRI -> {
                        if (!DatabaseHandler.getInstance().isRemoteHijriReady()) {
                            requestEvents(item, Constants.HIJRI_ENDPOINT)
                        }

                    }
                    CalendarType.GREGORIAN -> {
                        if (!DatabaseHandler.getInstance().isRemoteGregorianReady()) {
                            requestEvents(item, Constants.GREGORIAN_ENDPOINT)
                        }
                    }
                }
            }
        }

        private fun requestEvents(calendarType: CalendarType, endpoint: String) {
            apiService = ApiClient.getClient()!!.create(ApiService::class.java)
            val subscribe = apiService.getJalaliEvents(endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handleResponse(it, calendarType)
                }, this::handleError)
        }

        private fun handleResponse(
            response: com.github.persiancal.core.base.EventsResponse,
            calendarType: CalendarType
        ) {
            val events = response.events
            when (calendarType) {
                CalendarType.JALALI -> storeJalaliEvents(events)
                CalendarType.HIJRI -> storeHijriEvents(events)
                CalendarType.GREGORIAN -> storeGregorianEvents(events)
            }
        }

        private fun storeJalaliEvents(events: List<com.github.persiancal.core.base.EventsItem?>?) {
            events?.forEach { item ->
                var holidayIran = item?.holiday?.iran ?: listOf()

                val jalaliDb = item?.run {
                    RemoteJalaliEventsDb(
                        0,
                        key,
                        calendar,
                        month,
                        sources,
                        year,
                        description?.faIR,
                        title?.faIR,
                        day,
                        holidayIran
                    )
                }
                jalaliDb?.let { DatabaseHandler.getInstance().putRemoteJalaliEvents(it) }
            }
        }

        private fun storeHijriEvents(events: List<com.github.persiancal.core.base.EventsItem?>?) {
            events?.forEach { item ->
                var holidayIran = item?.holiday?.iran ?: listOf<String>()

                val hijriDb = item?.run {
                    RemoteHijriEventsDb(
                        0,
                        key,
                        calendar,
                        month,
                        sources,
                        year,
                        description?.faIR,
                        title?.faIR,
                        day,
                        holidayIran
                    )
                }
                hijriDb?.let { DatabaseHandler.getInstance().putRemoteHijriEvents(it) }
            }
        }

        private fun storeGregorianEvents(events: List<com.github.persiancal.core.base.EventsItem?>?) {
            events?.forEach { item ->
                var holidayIran = item?.holiday?.iran ?: listOf()

                val gregorianDb = item?.run {
                    RemoteGregorianEventsDb(
                        0,
                        key,
                        calendar,
                        month,
                        sources,
                        year,
                        description?.faIR,
                        title?.faIR,
                        day,
                        holidayIran
                    )
                }
                gregorianDb?.let { DatabaseHandler.getInstance().putRemoteGregorianEvents(it) }
            }
        }

        private fun handleError(error: Throwable) {
            error.printStackTrace()
        }

        fun getInstance(): RemoteCalendarEvents {
            return instance
        }

        fun addCalendar(calendarType: CalendarType): Companion {
            calendarTypeList.add(calendarType)
            return this
        }
    }
}