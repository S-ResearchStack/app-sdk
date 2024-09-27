package researchstack.data.datasource.healthConnect

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.reflect.KClass

class HealthConnectDataSource @Inject constructor(private val healthConnectClient: HealthConnectClient) {
    suspend fun <T : Record> getData(recordClass: KClass<out T>, duration: Long = 2): List<Record> {
        val currentDate: LocalDateTime = LocalDateTime.now().with(LocalTime.MIDNIGHT)
        val startTime: Instant =
            currentDate.minusDays(duration).atZone(ZoneId.systemDefault()).toInstant()
        val endTime: Instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()

        val result = healthConnectClient.readRecords(
            ReadRecordsRequest(
                recordClass,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
        )
        return result.records
    }
}
