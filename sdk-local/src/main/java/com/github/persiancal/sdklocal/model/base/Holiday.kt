package com.github.persiancal.sdklocal.model.base

import com.google.gson.annotations.SerializedName

data class Holiday(

    @field:SerializedName("Iran")
    val iran: List<String>? = null
)