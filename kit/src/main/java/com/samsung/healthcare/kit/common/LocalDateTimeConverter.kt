package com.samsung.healthcare.kit.common

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime

@ProvidedTypeConverter
class LocalDateTimeConverter {
    @TypeConverter
    fun timeToString(value: LocalDateTime?): String? = value?.toString()

    @TypeConverter
    fun stringToTime(value: String?): LocalDateTime? =
        try {
            LocalDateTime.parse(value)
        } catch (e: Exception) {
            null
        }
}
