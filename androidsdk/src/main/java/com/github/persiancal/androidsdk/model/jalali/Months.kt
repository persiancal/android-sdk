package com.github.persiancal.androidsdk.model.jalali

import com.google.gson.annotations.SerializedName

data class Months(

    @field:SerializedName("normal")
    val normal: List<Int?>? = null,

    @field:SerializedName("name")
    val name: List<NameItem?>? = null,

    @field:SerializedName("leap")
    val leap: List<Int?>? = null
)