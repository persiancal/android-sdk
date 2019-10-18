package com.github.persiancal.sdklocal.model.base

import com.google.gson.annotations.SerializedName

data class EventsItem(

    @field:SerializedName("calendar")
    val calendar: List<String?>? = null,

    @field:SerializedName("month")
    val month: Int? = null,

    @field:SerializedName("sources")
    val sources: List<String?>? = null,

    @field:SerializedName("year")
    val year: Int? = null,

    @field:SerializedName("description")
    val description: Description? = null,

    @field:SerializedName("title")
    val title: Title? = null,

    @field:SerializedName("day")
    val day: Int? = null,

    @field:SerializedName("holiday")
    val holiday: Holiday? = null,

    @field:SerializedName("key")
    val key: Long? = null
)