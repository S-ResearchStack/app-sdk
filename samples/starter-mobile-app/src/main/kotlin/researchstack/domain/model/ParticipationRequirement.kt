package researchstack.domain.model

import researchstack.domain.model.eligibilitytest.EligibilityTest
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType

data class ParticipationRequirement(
    val SHealthDataTypes: List<SHealthDataType>,
    val trackerDataTypes: List<TrackerDataType>,
    val privDataTypes: List<PrivDataType>,
    val deviceStatDataTypes: List<DeviceStatDataType>,
    val eligibilityTest: EligibilityTest,
    val informedConsent: InformedConsent
)
