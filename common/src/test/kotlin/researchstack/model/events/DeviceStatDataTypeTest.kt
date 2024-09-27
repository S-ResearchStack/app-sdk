package researchstack.model.events

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.events.MobileWearConnection
import researchstack.domain.model.events.WearableBattery
import researchstack.domain.model.events.WearableOffBody
import researchstack.domain.model.events.WearablePowerState

class DeviceStatDataTypeTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `DeviceStatDataType fromModel should work fine`() {
        val modelWearableOffBody = DeviceStatDataType.fromModel(WearableOffBody::class)
        assertEquals(DeviceStatDataType.WEAR_OFF_BODY, modelWearableOffBody)

        val modelWearableBattery = DeviceStatDataType.fromModel(WearableBattery::class)
        assertEquals(DeviceStatDataType.WEAR_BATTERY, modelWearableBattery)

        val modelWearablePowerState = DeviceStatDataType.fromModel(WearablePowerState::class)
        assertEquals(DeviceStatDataType.WEAR_POWER_ON_OFF, modelWearablePowerState)

        val modelMobileWearConnection = DeviceStatDataType.fromModel(MobileWearConnection::class)
        assertEquals(DeviceStatDataType.MOBILE_WEAR_CONNECTION, modelMobileWearConnection)

        val modelUnknown = DeviceStatDataType.fromModel(UnknownClass::class)
        assertNull(modelUnknown)
    }
}

class UnknownClass
