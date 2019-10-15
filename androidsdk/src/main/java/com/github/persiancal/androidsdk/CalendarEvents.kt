package com.github.persiancal.androidsdk

import android.content.Context
import com.github.persiancal.androidsdk.model.jalali.JalaliResponse
import com.github.persiancal.androidsdk.service.ApiService
import com.github.persiancal.androidsdk.util.ApiClient
import io.objectbox.BoxStore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CalendarEvents {
    init {
        instance = this
    }

    companion object {
        lateinit var boxStore: BoxStore
            private set

        private var instance: CalendarEvents? = null
        private lateinit var apiService: ApiService

        fun init(context: Context) {
//            boxStore = MyObjectBox.builder()
//                .androidContext(context.applicationContext)
//                .build()

            apiService = ApiClient.getClient()!!.create(ApiService::class.java)
            val subscribe = apiService.getJalaliEvents("jalali.json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError)
        }

        private fun handleResponse(jalaliResponse: JalaliResponse) {

        }

        private fun handleError(error: Throwable) {
            error.printStackTrace()
        }

        fun getInstance(): CalendarEvents {
            return instance!!
        }
    }
}