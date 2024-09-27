package researchstack.util

import android.app.AlarmManager
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.TimeZone

fun LocalDateTime.toEpochMilli() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
fun ZoneOffset.toEpochMilli() = this.totalSeconds.times(1000L)

fun getCurrentTimeOffset(): Int {
    val currentTime = System.currentTimeMillis()
    val tz = TimeZone.getDefault()
    return tz.getOffset(currentTime)
}

fun hourToTimestamp(hour: Int): Long {
    val calendar = Calendar.getInstance()

    val current = LocalDateTime.of(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DATE),
        hour,
        0,
        0,
    )
    if (current.toEpochMilli() < System.currentTimeMillis()) {
        return current.toEpochMilli() + AlarmManager.INTERVAL_DAY
    }
    return current.toEpochMilli()
}

fun getDayEndTime(dayStartTime: Long): Long {
    val millisecondsInADay = 24 * 60 * 60 * 1000
    return dayStartTime + millisecondsInADay - 1
}
