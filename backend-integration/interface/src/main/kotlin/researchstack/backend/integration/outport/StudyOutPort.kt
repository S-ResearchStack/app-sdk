package researchstack.backend.integration.outport

import researchstack.backend.grpc.EligibilityTestResult
import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.SignedInformedConsent
import researchstack.backend.grpc.Study

interface StudyOutPort {
    suspend fun getPublicStudyList(): Result<List<Study>>
    suspend fun getParticipatedStudyList(): Result<List<Study>>
    suspend fun getStudyByParticipationCode(participationCode: String): Result<Study>
    suspend fun getParticipationRequirementList(studyId: String): Result<GetParticipationRequirementListResponse>
    suspend fun participateInStudy(studyId: String, eligibilityTestResult: EligibilityTestResult, signedInformedConsent: SignedInformedConsent): Result<String>
    suspend fun withdrawFromStudy(studyId: String): Result<Unit>
}
