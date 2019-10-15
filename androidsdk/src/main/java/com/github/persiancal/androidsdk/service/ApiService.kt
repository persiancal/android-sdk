package com.github.persiancal.androidsdk.service

import com.github.persiancal.androidsdk.model.jalali.JalaliResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{endpoint}")
    fun getJalaliEvents(@Path("endpoint") get: String): Single<JalaliResponse>
}