package com.github.persiancal.core.base

import com.google.gson.annotations.SerializedName

data class CalendarsItem(

    @field:SerializedName("en_US")
    val enUS: String? = null,

    @field:SerializedName("fa_IR")
    val faIR: String? = null
)