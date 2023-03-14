package healthstack.healthdata.link.healthconnect

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SleepStageRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ChangesResponse
import androidx.health.connect.client.response.ReadRecordsResponse
import androidx.health.platform.client.permission.Permission
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
@OptIn(ExperimentalCoroutinesApi::class)
class HealthConnectAdapterTest {

    private lateinit var healthDataTypeStrings: List<String>
    private lateinit var healthConnectClientStub: HealthConnectClient
    private lateinit var healthConnectAdapter: HealthConnectAdapter
    private lateinit var readRecordsResponse: ReadRecordsResponse<out Record>

    @BeforeAll
    fun beforeAll() {
        healthDataTypeStrings = listOf("HeartRate")
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
                "HeartRate"
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

    @Tag("positive")
    @Test
    fun `test health connect adapter read sleep stage records`() {
        val startTime = Instant.parse("2023-01-01T01:00:00.000Z")
        val endTime = startTime.plus(10, ChronoUnit.MINUTES)
        val sleepSessionRecord = SleepStageRecord(
            startTime,
            null,
            endTime,
            null,
            stage = "sleeping"
        )

        runTest {
            // TODO: check permissions

            `when`(readRecordsResponse.records)
                .thenReturn(listOf(sleepSessionRecord))

            `when`(healthConnectClientStub.readRecords(any<ReadRecordsRequest<out Record>>()))
                .thenReturn(readRecordsResponse)

            val healthData = healthConnectAdapter.getHealthData(
                startTime.minusSeconds(1),
                endTime.plusSeconds(1),
                "SleepStage"
            )
            assertEquals(startTime, healthData.data[0]["startTime"])
            assertEquals(endTime, healthData.data[0]["endTime"])
            assertEquals(sleepSessionRecord.stage, healthData.data[0]["stage"])
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

    @Tag("negative")
    @Test
    fun `getHealthData should throw IllegalArgumentException when endTime is before startTime`() {
        val startTime = Instant.parse("2023-01-01T00:00:00.000Z")

        runTest {
            assertThrows<IllegalArgumentException> {
                healthConnectAdapter.getHealthData(
                    startTime,
                    startTime.minusSeconds(600),
                    "SleepStage"
                )
            }
        }
    }

    @Tag("positive")
    @Test
    fun `getChangesToken should return token string`() {
        runTest {
            val dataType = "SleepStage"
            val token = "token-string"
            `when`(
                healthConnectClientStub.getChangesToken(any())
            ).thenReturn(token)
            val actualToken = healthConnectAdapter.getChangesToken(dataType)
            assertEquals(token, actualToken)
        }
    }

    @Tag("negative")
    @Test
    fun `getChangesToken should throw IllegalArgumentException when health-data-type is not valid`() {
        runTest {
            assertThrows<IllegalArgumentException> {
                healthConnectAdapter.getChangesToken("invalid-health-type")
            }
        }
    }

    @Tag("positive")
    @Test
    fun `getChanges should return changed data`() {
        val changeResponse = mock<ChangesResponse>()
        `when`(
            changeResponse.nextChangesToken
        ).thenReturn("new-token-string")
        `when`(
            changeResponse.changes
        ).thenReturn(emptyList())

        runTest {
            val dataType = "SleepSession"
            val token = "valid-token-string"
            `when`(
                healthConnectClientStub.getChanges(token)
            ).thenReturn(changeResponse)
            val changes = healthConnectAdapter.getChanges(token, dataType)
            assertNotNull(changes.token)
            assertNotNull(dataType, changes.healthData.type)
        }
    }

    @Tag("negative")
    @Test
    fun `getChanges should throw IllegalArgumentException when health-data-type is not valid`() {
        runTest {
            assertThrows<IllegalArgumentException> {
                healthConnectAdapter.getChanges("valid-token", "invalid-type")
            }
        }
    }

    @Tag("positive")
    @Test
    fun `hasAllPermissions should return false when returned permissions is equal to request permissions`() {
        runTest {
            val permissions = healthDataTypeStrings.map {
                listOf(
                    HealthPermission.createReadPermission(HealthConnectUtils.nameToRecord(it)),
                    HealthPermission.createWritePermission(HealthConnectUtils.nameToRecord(it))
                )
            }
                .flatten()
                .toSet()

            val permissionController = mock<PermissionController>()
            `when`(
                healthConnectClientStub.permissionController
            ).thenReturn(permissionController)

            `when`(permissionController.getGrantedPermissions(permissions))
                .thenReturn(permissions)

            assertTrue(healthConnectAdapter.hasAllPermissions())
        }
    }
}
