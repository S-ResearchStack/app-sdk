package researchstack.domain.model.sensor

import researchstack.domain.model.shealth.HealthTypeEnum

enum class TrackerDataType : HealthTypeEnum {
    LIGHT,
    ACCELEROMETER,
    SPEED
}
