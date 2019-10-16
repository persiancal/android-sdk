package com.github.persiancal.sdkremote

import android.content.Context
import android.util.Log
import com.github.persiancal.sdkremote.model.jalali.JalaliResponse
import com.github.persiancal.sdkremote.model.jalali.MyObjectBox
import com.github.persiancal.sdkremote.model.jalali.RemoteJalaliEventsDb
import com.github.persiancal.sdkremote.model.jalali.RemoteJalaliEventsDb_
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

        fun init(
            context: Context,
            calendarType: CalendarType
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
            val endpoint = when (calendarType) {
                CalendarType.JALALI -> Constants.JALALI_ENDPOINT
                CalendarType.HIJRI -> Constants.HIJRI_ENDPOINT
                CalendarType.GREGORIAN -> Constants.GREGORIAN_ENDPOINT
            }
            if (!getInstance().isReady()) {
                apiService = ApiClient.getClient()!!.create(ApiService::class.java)
                val subscribe = apiService.getJalaliEvents(endpoint)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse, this::handleError)
            }
        }

        private fun handleResponse(jalaliResponse: JalaliResponse) {
            val events = jalaliResponse.events
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

        private fun handleError(error: Throwable) {
            error.printStackTrace()
        }

        fun getInstance(): RemoteCalendarEvents {
            return instance
        }

    }

    fun isReady(): Boolean {
        return remoteJalaliEventsDbBox.count() != 0L
    }

    fun getJalaliEvents(dayOfMonth: Int, month: Int): MutableList<RemoteJalaliEventsDb>? {
        val query = remoteJalaliEventsDbBox.query {
            equal(RemoteJalaliEventsDb_.month, month)
            equal(RemoteJalaliEventsDb_.day, dayOfMonth)
        }
        return query.find()
    }
}