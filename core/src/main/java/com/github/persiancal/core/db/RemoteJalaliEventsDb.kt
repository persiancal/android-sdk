package com.github.persiancal.core.db

import com.github.persiancal.core.StringListConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class RemoteJalaliEventsDb(
    @Id
    var id: Long = 0,
    val key: Long?,
    @Convert(converter = StringListConverter::class, dbType = String::class)
    val calendar: List<String?>? = null,
    val month: Int? = null,
    @Convert(converter = StringListConverter::class, dbType = String::class)
    val sources: List<String?>? = null,
    val year: Int? = null,
    val description_fa_IR: String? = null,
    val title_fa_IR: String? = null,
    val day: Int? = null,
    @Convert(converter = StringListConverter::class, dbType = String::class)
    val holiday_Iran: List<String>? = null
)