package researchstack.data.datasource.grpc

import io.grpc.Status
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.data.datasource.grpc.mapper.toData
import researchstack.data.datasource.grpc.mapper.toDomain
import researchstack.domain.exception.AlreadyJoinedStudy
import researchstack.domain.exception.EmptyStudyCodeException
import researchstack.domain.exception.NotFoundStudyException
import researchstack.domain.exception.UnableToResolveHostException
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.Study
import researchstack.domain.model.eligibilitytest.EligibilityTestResult

class GrpcStudyDataSource(private val studyOutPort: StudyOutPort) {
    suspend fun getStudyList(): Result<List<Study>> = studyOutPort.getPublicStudyList().map { it.map { study -> study.toDomain() } }

    suspend fun getJoinedStudyList(): Result<List<Study>> = studyOutPort.getParticipatedStudyList().map { it.map { study -> study.toDomain() } }

    suspend fun getStudyByParticipationCode(participationCode: String): Result<Study> =
            studyOutPort.getStudyByParticipationCode(
                participationCode
            ).map { it.toDomain() }
        .recoverCatching { ex ->
            val msg = ex.message ?: ""
            when (Status.fromThrowable(ex).code) {
                Status.NOT_FOUND.code -> throw NotFoundStudyException
                Status.INVALID_ARGUMENT.code -> throw EmptyStudyCodeException
                Status.UNAVAILABLE.code ->
                    if (UNABLE_TO_RESOLVE_HOST in msg) throw UnableToResolveHostException
                    else throw ex
                else -> throw ex
            }
        }

    suspend fun getParticipationRequirement(studyId: String): Result<ParticipationRequirement> =
            studyOutPort.getParticipationRequirementList(studyId).map { it.toDomain(studyId) }

    suspend fun participateInStudy(
        studyId: String,
        eligibilityTestResult: EligibilityTestResult,
        signedInformedConsent: InformedConsent,
    ): Result<String> =
            studyOutPort.participateInStudy(studyId, eligibilityTestResult.toData(), signedInformedConsent.toData())
        .recoverCatching { ex ->
            if (Status.fromThrowable(ex).code == Status.ALREADY_EXISTS.code) throw AlreadyJoinedStudy
            throw ex
        }

    suspend fun withdrawFromStudy(
        studyId: String,
    ): Result<Unit> = studyOutPort.withdrawFromStudy(studyId)

    companion object {
        private val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
    }
}
