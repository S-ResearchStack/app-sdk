package researchstack.wearable.standalone.data.datasource.grpc.mapper

import com.google.protobuf.Timestamp
import com.google.protobuf.util.Timestamps.fromMillis
import researchstack.util.toEpochMilli
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date
import com.google.type.Date as GoogleDate
import com.google.type.TimeOfDay as GoogleTimeOfDay

fun GoogleDate.toLocalDate(): LocalDate = LocalDate.of(year, month, day)

fun LocalDate.toDate(): GoogleDate =
    GoogleDate.newBuilder().setYear(year).setMonth(monthValue).setDay(dayOfMonth).build()

fun LocalDateTime.toTimeStamp(): Timestamp =
    fromMillis(this.toEpochMilli())

fun Timestamp.toDate(): Date =
    Date.from(Instant.ofEpochSecond(seconds, nanos.toLong()))

fun GoogleTimeOfDay.toLocalTime(): LocalTime =
    LocalTime.of(hours, minutes, seconds)
