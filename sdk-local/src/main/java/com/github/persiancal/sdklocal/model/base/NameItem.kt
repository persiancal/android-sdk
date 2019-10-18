package com.github.persiancal.sdklocal.model.base

import com.google.gson.annotations.SerializedName

data class NameItem(

    @field:SerializedName("en_US")
    val enUS: String? = null,

    @field:SerializedName("fa_IR")
    val faIR: String? = null
)