package com.github.persiancal.sdkremote.model.base

import com.google.gson.annotations.SerializedName

data class Holiday(

    @field:SerializedName("Iran")
    val iran: List<String>? = null
)