package com.github.persiancal.sdkremote

import android.content.Context
import android.util.Log
import com.github.persiancal.sdkremote.model.*
import com.github.persiancal.sdkremote.model.base.EventsItem
import com.github.persiancal.sdkremote.model.base.EventsResponse
import com.github.persiancal.sdkremote.service.ApiService
import com.github.persiancal.sdkremote.util.ApiClient
import com.github.persiancal.sdkremote.util.Constants
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RemoteCalendarEvents {

    companion object {
        private var instance: RemoteCalendarEvents = RemoteCalendarEvents()
        private lateinit var boxStore: BoxStore
        private lateinit var apiService: ApiService
        private lateinit var remoteJalaliEventsDbBox: Box<RemoteJalaliEventsDb>
        private lateinit var remoteHijriEventsDbBox: Box<RemoteHijriEventsDb>
        private lateinit var remoteGregorianEventsDbBox: Box<RemoteGregorianEventsDb>
        private var calendarTypeList = arrayListOf<CalendarType>()

        fun init(
            context: Context
        ) {
            boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
            if (BuildConfig.DEBUG) {
                val started = AndroidObjectBrowser(boxStore).start(context)
                Log.i("ObjectBrowser", "Started: $started")
            }
            remoteJalaliEventsDbBox = boxStore.boxFor(
                RemoteJalaliEventsDb::class.java
            )
            remoteHijriEventsDbBox = boxStore.boxFor(
                RemoteHijriEventsDb::class.java
            )
            remoteGregorianEventsDbBox = boxStore.boxFor(
                RemoteGregorianEventsDb::class.java
            )
            for (item in calendarTypeList) {
                when (item) {
                    CalendarType.JALALI -> {
                        if (!getInstance().isJalaliReady()) {
                            requestEvents(item, Constants.JALALI_ENDPOINT)
                        }
                    }
                    CalendarType.HIJRI -> {
                        if (!getInstance().isHijriReady()) {
                            requestEvents(item, Constants.HIJRI_ENDPOINT)
                        }

                    }
                    CalendarType.GREGORIAN -> {
                        if (!getInstance().isGregorianReady()) {
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
            response: EventsResponse,
            calendarType: CalendarType
        ) {
            val events = response.events
            when (calendarType) {
                CalendarType.JALALI -> storeJalaliEvents(events)
                CalendarType.HIJRI -> storeHijriEvents(events)
                CalendarType.GREGORIAN -> storeGregorianEvents(events)
            }
        }

        private fun storeJalaliEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
                val jalaliDb = RemoteJalaliEventsDb(
                    0,
                    item.key!!.toLong(),
                    item.calendar,
                    item.month,
                    item.sources,
                    item.year,
                    item.description!!.faIR,
                    item.title!!.faIR,
                    item.day,
                    holidayIran
                )
                remoteJalaliEventsDbBox.put(jalaliDb)
            }
        }

        private fun storeHijriEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
                val hijriDb = RemoteHijriEventsDb(
                    0,
                    item.key!!.toLong(),
                    item.calendar,
                    item.month,
                    item.sources,
                    item.year,
                    item.description!!.faIR,
                    item.title!!.faIR,
                    item.day,
                    holidayIran
                )
                remoteHijriEventsDbBox.put(hijriDb)
            }
        }

        private fun storeGregorianEvents(events: List<EventsItem?>?) {
            for (item in events!!) {
                var holidayIran = listOf<String>()
                if (item!!.holiday != null) {
                    holidayIran = item.holiday!!.iran!!
                }
                val gregorianDb = RemoteGregorianEventsDb(
                    0,
                    item.key!!.toLong(),
                    item.calendar,
                    item.month,
                    item.sources,
                    item.year,
                    item.description!!.faIR,
                    item.title!!.faIR,
                    item.day,
                    holidayIran
                )
                remoteGregorianEventsDbBox.put(gregorianDb)
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

    fun isJalaliReady(): Boolean {
        return remoteJalaliEventsDbBox.count() != 0L
    }

    fun isHijriReady(): Boolean {
        return remoteHijriEventsDbBox.count() != 0L
    }

    fun isGregorianReady(): Boolean {
        return remoteGregorianEventsDbBox.count() != 0L
    }

    fun getJalaliEvents(dayOfMonth: Int, month: Int): MutableList<RemoteJalaliEventsDb>? {
        val query = remoteJalaliEventsDbBox.query {
            equal(RemoteJalaliEventsDb_.month, month)
            equal(RemoteJalaliEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun getHijriEvents(dayOfMonth: Int, month: Int): MutableList<RemoteHijriEventsDb>? {
        val query = remoteHijriEventsDbBox.query {
            equal(RemoteHijriEventsDb_.month, month)
            equal(RemoteHijriEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }

    fun getGregorianEvents(dayOfMonth: Int, month: Int): MutableList<RemoteGregorianEventsDb>? {
        val query = remoteGregorianEventsDbBox.query {
            equal(RemoteGregorianEventsDb_.month, month)
            equal(RemoteGregorianEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }
}