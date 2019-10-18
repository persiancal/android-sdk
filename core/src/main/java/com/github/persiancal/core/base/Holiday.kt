package com.github.persiancal.core.base

import com.google.gson.annotations.SerializedName

data class Holiday(

    @field:SerializedName("Iran")
    val iran: List<String>? = null
)