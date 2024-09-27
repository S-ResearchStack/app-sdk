package researchstack.data.datasource.grpc.mapper

import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.model.shealth.SHealthDataType

@TestInstance(PER_CLASS)
class HealthDataGrpcMapperTest {
    private var healthDataModel = spyk(HealthDataModel(SHealthDataType.UNSPECIFIED, listOf()))

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `DomainHealthDataType#toGrpcHealthDataType should not throw Exception`() {
        SHealthDataType.values().forEach {
            healthDataModel = HealthDataModel(it, listOf())
            Assertions.assertNotNull(healthDataModel.toData())
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `PrivDataType#toGrpcHealthDataType should not throw Exception`() {
        PrivDataType.values().forEach {
            healthDataModel = HealthDataModel(it, listOf())
            Assertions.assertNotNull(healthDataModel.toData())
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `TrackerDataType#toGrpcHealthDataType should not throw Exception`() {
        listOf(TrackerDataType.LIGHT, TrackerDataType.ACCELEROMETER).forEach {
            healthDataModel = HealthDataModel(it, listOf())
            Assertions.assertNotNull(
                healthDataModel.toData()
            )
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `TrackerDataType#toGrpcHealthDataType should throw Exception`() {
        healthDataModel = HealthDataModel(TrackerDataType.SPEED, listOf())
        Assertions.assertThrows(NotImplementedError::class.java) {
            healthDataModel.toData()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `DeviceStatDataType#toGrpcHealthDataType should not throw Exception`() {
        DeviceStatDataType.values().forEach {
            healthDataModel = HealthDataModel(it, listOf())
            Assertions.assertNotNull(healthDataModel.toData())
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `HealthDataModel#dataList should throw Exception`() {
        Assertions.assertThrows(ArithmeticException::class.java) {
            healthDataModel = HealthDataModel(DeviceStatDataType.WEAR_BATTERY, listOf(mapOf("abc" to 8 / 0)))
        }
    }
}
