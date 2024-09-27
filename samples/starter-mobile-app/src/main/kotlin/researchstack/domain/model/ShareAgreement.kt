package researchstack.domain.model

import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType

data class ShareAgreement(
    val studyId: String,
    val dataType: Enum<*>,
    val approval: Boolean = false,
) {
    init {
        val allowedEnums = listOf(
            SHealthDataType::class,
            TrackerDataType::class,
            PrivDataType::class,
            DeviceStatDataType::class,
        )

        if (allowedEnums.any { it.isInstance(dataType) }.not())
            throw IllegalArgumentException("Only type in ${allowedEnums.map { it.simpleName }} can be unifiedDataType")
    }
}
