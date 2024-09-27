package researchstack.domain.model.healthConnect

import researchstack.domain.model.shealth.HealthTypeEnum

enum class HealthConnectDataType : HealthTypeEnum {
    BLOOD_GLUCOSE,
    BLOOD_PRESSURE,
    EXERCISE,
    HEART_RATE,
    OXYGEN_SATURATION,
    SLEEP_SESSION,
    STEPS,
}
