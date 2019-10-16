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
        private lateinit var selectedCalendarType: CalendarType

        fun init(
            context: Context,
            calendarType: CalendarType
        ) {
            selectedCalendarType = calendarType
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
            when (calendarType) {
                CalendarType.JALALI -> {
                    if (!getInstance().isJalaliReady()) {
                        requestEvents(Constants.JALALI_ENDPOINT)
                    }
                }
                CalendarType.HIJRI -> {
                    if (!getInstance().isHijriReady()) {
                        requestEvents(Constants.HIJRI_ENDPOINT)
                    }

                }
                CalendarType.GREGORIAN -> {
                    if (!getInstance().isGregorianReady()) {
                        requestEvents(Constants.GREGORIAN_ENDPOINT)
                    }
                }
            }

        }

        private fun requestEvents(endpoint: String) {
            apiService = ApiClient.getClient()!!.create(ApiService::class.java)
            val subscribe = apiService.getJalaliEvents(endpoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        }

        private fun handleResponse(eventsResponse: EventsResponse) {
            val events = eventsResponse.events
            when (selectedCalendarType) {
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

        }

        private fun handleError(error: Throwable) {
            error.printStackTrace()
        }

        fun getInstance(): RemoteCalendarEvents {
            return instance
        }

    }

    fun isJalaliReady(): Boolean {
        return remoteJalaliEventsDbBox.count() != 0L
    }

    fun isHijriReady(): Boolean {
        return remoteHijriEventsDbBox.count() != 0L
    }

    fun isGregorianReady(): Boolean {
        return false
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
}