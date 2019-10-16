package com.github.persiancal.sdkremote.model.base

import com.google.gson.annotations.SerializedName

data class EventsResponse(

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