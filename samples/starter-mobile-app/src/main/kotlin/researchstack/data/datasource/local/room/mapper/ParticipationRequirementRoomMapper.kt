package researchstack.data.datasource.local.room.mapper

import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.eligibilitytest.EligibilityTest

fun ParticipationRequirementEntity.toDomain(): ParticipationRequirement = ParticipationRequirement(
    SHealthDataTypes = SHealthDataTypes,
    trackerDataTypes = trackerDataTypes,
    privDataTypes = privDataTypes,
    deviceStatDataTypes = deviceStatDataTypes,
    eligibilityTest = EligibilityTest(
        studyId,
        answers = answers,
        sections = sections
    ),
    informedConsent = InformedConsent(studyId, informedConsentEntity.informedConsentUrl)
)
