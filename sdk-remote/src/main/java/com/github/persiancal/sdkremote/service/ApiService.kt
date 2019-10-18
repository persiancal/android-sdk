package com.github.persiancal.sdkremote.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{endpoint}")
    fun getJalaliEvents(@Path("endpoint") get: String): Single<com.github.persiancal.core.base.EventsResponse>
}