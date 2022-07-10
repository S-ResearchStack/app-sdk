package com.samsung.healthcare.kit.common

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

object TimeUtil {
    fun stringToDate(localDateTimeString: String): Date =
        Date.from(LocalDateTime.parse(localDateTimeString).atZone(ZoneId.systemDefault()).toInstant())

    fun dateToLocalDateTimeSystem(date: Date): LocalDateTime =
        date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
}
