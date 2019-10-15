package com.github.persiancal.androidsdk

import android.content.Context
import com.github.persiancal.androidsdk.model.jalali.JalaliEventsDb
import com.github.persiancal.androidsdk.model.jalali.JalaliEventsDb_
import com.github.persiancal.androidsdk.model.jalali.JalaliResponse
import com.github.persiancal.androidsdk.model.jalali.MyObjectBox
import com.github.persiancal.androidsdk.service.ApiService
import com.github.persiancal.androidsdk.util.ApiClient
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.equal
import io.objectbox.kotlin.query
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CalendarEvents {

    companion object {
        private var instance: CalendarEvents = CalendarEvents()
        private lateinit var boxStore: BoxStore
        private lateinit var apiService: ApiService
        private lateinit var jalaliEventsDbBox: Box<JalaliEventsDb>

        fun init(context: Context) {
            boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
            jalaliEventsDbBox = boxStore.boxFor(
                JalaliEventsDb::class.java
            )
            if (!getInstance().isReady()) {
                apiService = ApiClient.getClient()!!.create(ApiService::class.java)
                val subscribe = apiService.getJalaliEvents("jalali.json")
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
                val jalaliDb = JalaliEventsDb(
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
                jalaliEventsDbBox.put(jalaliDb)
            }
        }

        private fun handleError(error: Throwable) {
            error.printStackTrace()
        }

        fun getInstance(): CalendarEvents {
            return instance
        }

    }

    fun isReady(): Boolean {
        return jalaliEventsDbBox.count() != 0L
    }

    fun getJalaliEvents(month: Int, day: Int): MutableList<JalaliEventsDb>? {
        val query = jalaliEventsDbBox.query {
            equal(JalaliEventsDb_.month, month)
            equal(JalaliEventsDb_.day, day)
        }
        return query.find()
    }
}