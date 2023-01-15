package healthstack.healthdata.link.healthconnect

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.platform.client.permission.Permission
import java.time.Instant
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

@DisplayName("Health Connect Adapter Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthConnectAdapterTest {

    private lateinit var healthDataTypeStrings: List<String>
    private lateinit var healthConnectClientStub: HealthConnectClient
    private lateinit var healthConnectAdapter: HealthConnectAdapter
    private lateinit var readRecordsResponse: ReadRecordsResponse<out Record>

    @BeforeAll
    fun beforeAll() {
        healthDataTypeStrings = listOf("HeartRateSeries")
        healthConnectClientStub = mock<HealthConnectClient>()

        val context = mock<ComponentActivity>()
        val launcher = mock<ActivityResultLauncher<Set<Permission>>>()

        `when`(
            context.registerForActivityResult(
                any<ActivityResultContract<Set<Permission>, Set<Permission>>>(),
                any<ActivityResultCallback<Set<Permission>>>()
            )
        ).thenReturn(launcher)

        healthConnectAdapter = HealthConnectAdapter(
            healthDataTypeStrings, healthConnectClientStub
        )

        readRecordsResponse = mock<ReadRecordsResponse<out Record>>()
    }

    @Tag("positive")
    @Test
    fun `test health connect adapter read series records`() {
        val requestStartTime = Instant.parse("2023-01-01T00:00:00.000Z")
        val requestEndTime = Instant.parse("2023-01-02T00:00:00.000Z")

        val seriesStartTime = Instant.parse("2023-01-01T12:00:00.000Z")
        val seriesEndTime = Instant.parse("2023-01-01T12:10:00.000Z")
        val sampleTime = Instant.parse("2023-01-01T12:05:00.000Z")
        val sampleBPM: Long = 100

        val heartRateRecord = HeartRateRecord(
            seriesStartTime,
            null,
            seriesEndTime,
            null,
            listOf(HeartRateRecord.Sample(sampleTime, 100))
        )

        runTest {
            // TODO: check permissions

            `when`(readRecordsResponse.records)
                .thenReturn(listOf(heartRateRecord))

            `when`(healthConnectClientStub.readRecords(any<ReadRecordsRequest<out Record>>()))
                .thenReturn(readRecordsResponse)

            val healthData = healthConnectAdapter.getHealthData(
                requestStartTime,
                requestEndTime,
                "HeartRateSeries"
            )

            assertEquals(sampleBPM, healthData.data[0]["bpm"])
            assertEquals(sampleTime, healthData.data[0]["time"])
        }
    }

    @Tag("positive")
    @Test
    fun `test health connect adapter read sleep session records`() {
        val sleepAt = Instant.parse("2023-01-01T01:00:00.000Z")
        val awakeAt = Instant.parse("2023-01-01T08:10:00.000Z")
        val sleepSessionRecord = SleepSessionRecord(
            sleepAt,
            null,
            awakeAt,
            null,
        )

        runTest {
            // TODO: check permissions

            `when`(readRecordsResponse.records)
                .thenReturn(listOf(sleepSessionRecord))

            `when`(healthConnectClientStub.readRecords(any<ReadRecordsRequest<out Record>>()))
                .thenReturn(readRecordsResponse)

            val healthData = healthConnectAdapter.getHealthData(
                sleepAt.minusSeconds(1),
                awakeAt.plusSeconds(1),
                "SleepSession"
            )
            assertEquals(sleepAt, healthData.data[0]["startTime"])
            assertEquals(awakeAt, healthData.data[0]["endTime"])
        }
    }

    @Tag("negative")
    @Test
    fun `getHealthData should throw IllegalArgumentException`() {
        val requestStartTime = Instant.parse("2023-01-01T00:00:00.000Z")
        val requestEndTime = Instant.parse("2023-01-02T00:00:00.000Z")

        val fakeHealthDataType = "fakeHealthDataType"
        runTest {
            // TODO: check permissions

            assertThrows<IllegalArgumentException> {
                healthConnectAdapter.getHealthData(
                    requestStartTime,
                    requestEndTime,
                    fakeHealthDataType
                )
            }
        }
    }
}
