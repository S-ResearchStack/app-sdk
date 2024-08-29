package healthstack.common.util

import java.util.TimeZone

fun getCurrentTimeOffset(): Int {
    val currentTime = System.currentTimeMillis()
    val tz = TimeZone.getDefault()
    return tz.getOffset(currentTime)
}
