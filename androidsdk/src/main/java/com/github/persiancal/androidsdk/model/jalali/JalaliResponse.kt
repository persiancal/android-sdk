package com.github.persiancal.androidsdk.model.jalali

import com.google.gson.annotations.SerializedName

data class JalaliResponse(

    @field:SerializedName("months")
    val months: Months? = null,

    @field:SerializedName("calendars")
    val calendars: List<CalendarsItem?>? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("countries")
    val countries: List<String?>? = null,

    @field:SerializedName("events")
    val events: List<EventsItem?>? = null
)