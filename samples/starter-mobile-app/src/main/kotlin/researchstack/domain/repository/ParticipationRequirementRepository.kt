package researchstack.domain.repository

import researchstack.domain.model.InformedConsent
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.eligibilitytest.EligibilityTestResult

interface ParticipationRequirementRepository {
    suspend fun getParticipationRequirement(studyId: String): Result<ParticipationRequirement>

    suspend fun fetchParticipationRequirementFromNetwork(studyId: String)

    suspend fun saveParticipationRequirementResultToLocal(
        studyId: String,
        eligibilityTestResult: EligibilityTestResult,
        signedInformedConsent: InformedConsent,
    ): Result<Unit>
}
