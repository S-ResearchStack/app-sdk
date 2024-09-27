package researchstack.domain.model.events

import researchstack.domain.model.shealth.HealthTypeEnum
import kotlin.reflect.KClass

enum class DeviceStatDataType : HealthTypeEnum {
    WEAR_OFF_BODY,
    WEAR_BATTERY,
    WEAR_POWER_ON_OFF,
    MOBILE_WEAR_CONNECTION;

    companion object {
        fun fromModel(model: KClass<*>) = when (model) {
            WearableOffBody::class -> WEAR_OFF_BODY
            WearableBattery::class -> WEAR_BATTERY
            WearablePowerState::class -> WEAR_POWER_ON_OFF
            MobileWearConnection::class -> MOBILE_WEAR_CONNECTION
            else -> null
        }
    }
}
