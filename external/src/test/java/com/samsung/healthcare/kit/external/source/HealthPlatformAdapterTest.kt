package com.samsung.healthcare.kit.external.source

import com.google.android.libraries.healthdata.HealthDataClient
import com.google.android.libraries.healthdata.data.IntervalData
import com.google.android.libraries.healthdata.data.IntervalDataSet
import com.google.android.libraries.healthdata.data.IntervalDataTypes
import com.google.android.libraries.healthdata.data.ReadDataRequest
import com.google.android.libraries.healthdata.data.ReadDataResponse
import com.google.android.libraries.healthdata.data.SampleData
import com.google.android.libraries.healthdata.data.SampleDataSet
import com.google.android.libraries.healthdata.data.SampleDataTypes
import com.google.android.libraries.healthdata.permission.AccessType
import com.google.android.libraries.healthdata.permission.Permission
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.guava.future
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Instant

@DisplayName("Health Platform Adapter Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@OptIn(ExperimentalCoroutinesApi::class)
class HealthPlatformAdapterTest {

    private lateinit var healthDataTypeStrings: List<String>
    private lateinit var healthDataClientStub: HealthDataClient
    private lateinit var healthPlatformAdapter: HealthPlatformAdapter
    private lateinit var heartRateReadPermission: Set<Permission>
    private lateinit var stepsReadPermission: Set<Permission>
    private lateinit var allRequiredPermissions: Set<Permission>

    @BeforeAll
    fun beforeAll() {
        healthDataTypeStrings = listOf("HeartRate", "Steps")

        healthDataClientStub = mock(HealthDataClient::class.java)

        healthPlatformAdapter =
            HealthPlatformAdapter.initialize(healthDataClientStub, healthDataTypeStrings)
                .run { HealthPlatformAdapter.getInstance() }

        heartRateReadPermission = hashSetOf(
            Permission.create(SampleDataTypes.HEART_RATE, AccessType.READ)
        )

        stepsReadPermission = hashSetOf(
            Permission.create(IntervalDataTypes.STEPS, AccessType.READ)
        )

        allRequiredPermissions = hashSetOf(
            Permission.create(SampleDataTypes.HEART_RATE, AccessType.READ),
            Permission.create(SampleDataTypes.HEART_RATE, AccessType.WRITE),
            Permission.create(IntervalDataTypes.STEPS, AccessType.READ),
            Permission.create(IntervalDataTypes.STEPS, AccessType.WRITE)
        )
    }

    @Test
    fun `test convert string to health data type`() {
        val fakeHealthData = "fakeHealthData"

        assertThrows<IllegalArgumentException> {
            HealthPlatformAdapter.convertStringToHealthDataType(fakeHealthData)
        }
    }

    @Test
    fun `test health platform adapter has permissions`() {
        runTest {
            val heartRateReadPermissionFuture: ListenableFuture<Set<Permission>> = future {
                heartRateReadPermission
            }

            `when`(healthDataClientStub.getGrantedPermissions(heartRateReadPermission))
                .thenReturn(heartRateReadPermissionFuture)

            assertTrue(healthPlatformAdapter.hasPermissions(heartRateReadPermission))
        }
    }

    @Test
    fun `test health platform adapter doesn't have permissions`() {
        runTest {
            val stepsReadPermissionFuture: ListenableFuture<Set<Permission>> = future {
                stepsReadPermission
            }

            `when`(healthDataClientStub.getGrantedPermissions(heartRateReadPermission))
                .thenReturn(stepsReadPermissionFuture)

            assertFalse(healthPlatformAdapter.hasPermissions(heartRateReadPermission))
        }
    }

    @Test
    fun `test health platform adapter has all permissions`() {
        runTest {
            val allRequiredPermissionsFuture: ListenableFuture<Set<Permission>> = future {
                allRequiredPermissions
            }

            `when`(healthDataClientStub.getGrantedPermissions(allRequiredPermissions))
                .thenReturn(allRequiredPermissionsFuture)

            assertTrue(healthPlatformAdapter.hasAllPermissions())
        }
    }

    @Test
    fun `test health platform adapter read sample data`() {
        val requestStartTime = "2022-05-25T10:10:20.274Z"
        val requestEndTime = "2022-05-25T10:30:30.274Z"

        runTest {
            val heartRateReadPermissionFuture: ListenableFuture<Set<Permission>> = future {
                heartRateReadPermission
            }

            val readDataResponse = ReadDataResponse.builder().addSampleDataSet(
                SampleDataSet.builder(SampleDataTypes.HEART_RATE)
                    .setStartTime(Instant.parse("2022-05-25T10:20:20.274Z"))
                    .setEndTime(Instant.parse("2022-05-25T10:20:30.274Z"))
                    .addData(
                        SampleData.builder(SampleDataTypes.HEART_RATE)
                            .setTime(Instant.parse("2022-05-25T10:20:29.274Z"))
                            .setLongValue(5)
                            .build()
                    ).build()
            ).build()

            val readDataResponseFuture: ListenableFuture<ReadDataResponse> = future {
                readDataResponse
            }

            `when`(healthDataClientStub.getGrantedPermissions(heartRateReadPermission))
                .thenReturn(heartRateReadPermissionFuture)

            `when`(healthDataClientStub.readData(any(ReadDataRequest::class.java)))
                .thenReturn(readDataResponseFuture)

            val readData = healthPlatformAdapter.getHealthData(
                requestStartTime,
                requestEndTime,
                "HeartRate"
            ).data

            assertEquals(readData[0]["bpm"], 5.toLong())
        }
    }

    @Test
    fun `test health platform adapter read interval data`() {
        val requestStartTime = "2022-05-25T10:10:20.274Z"
        val requestEndTime = "2022-05-25T10:30:30.274Z"

        runTest {
            val stepsReadPermissionFuture: ListenableFuture<Set<Permission>> = future {
                stepsReadPermission
            }

            val readDataResponse = ReadDataResponse.builder().addIntervalDataSet(
                IntervalDataSet.builder(IntervalDataTypes.STEPS)
                    .setStartTime(Instant.parse("2022-05-25T10:20:20.274Z"))
                    .setEndTime(Instant.parse("2022-05-25T10:20:30.274Z"))
                    .addData(
                        IntervalData.builder(IntervalDataTypes.STEPS)
                            .setStartTime(Instant.parse("2022-05-25T10:20:21.274Z"))
                            .setEndTime(Instant.parse("2022-05-25T10:20:23.274Z"))
                            .setLongValue(5)
                            .build()
                    ).build()
            ).build()

            val readDataResponseFuture: ListenableFuture<ReadDataResponse> = future {
                readDataResponse
            }

            `when`(healthDataClientStub.getGrantedPermissions(stepsReadPermission))
                .thenReturn(stepsReadPermissionFuture)

            `when`(healthDataClientStub.readData(any(ReadDataRequest::class.java)))
                .thenReturn(readDataResponseFuture)

            val readData =
                healthPlatformAdapter.getHealthData(requestStartTime, requestEndTime, "Steps").data

            assertEquals(readData[0]["count"], 5.toLong())
        }
    }
}
