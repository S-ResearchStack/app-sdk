package researchstack.backend.integration.adapter.outgoing

import com.google.protobuf.Empty
import researchstack.backend.grpc.EligibilityTestResult
import researchstack.backend.grpc.GetParticipationRequirementListRequest
import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.GetStudyByParticipationCodeRequest
import researchstack.backend.grpc.ParticipateInStudyRequest
import researchstack.backend.grpc.SignedInformedConsent
import researchstack.backend.grpc.Study
import researchstack.backend.grpc.StudyServiceGrpcKt
import researchstack.backend.grpc.WithdrawFromStudyRequest
import researchstack.backend.integration.outport.StudyOutPort
import javax.inject.Inject

class StudyAdapter @Inject constructor(
    private val studyServiceCoroutineStub: StudyServiceGrpcKt.StudyServiceCoroutineStub
) : StudyOutPort {
    override suspend fun getPublicStudyList(): Result<List<Study>> = runCatching {
        studyServiceCoroutineStub.getPublicStudyList(Empty.getDefaultInstance()).studiesList
    }

    override suspend fun getParticipatedStudyList(): Result<List<Study>> = runCatching {
        studyServiceCoroutineStub.getParticipatedStudyList(Empty.getDefaultInstance()).studiesList
    }

    override suspend fun getStudyByParticipationCode(participationCode: String): Result<Study> = runCatching {
        studyServiceCoroutineStub.getStudyByParticipationCode(
            GetStudyByParticipationCodeRequest.newBuilder()
                .setParticipationCode(participationCode).build()
        ).study
    }

    override suspend fun getParticipationRequirementList(studyId: String): Result<GetParticipationRequirementListResponse> = runCatching {
        studyServiceCoroutineStub.getParticipationRequirementList(
            GetParticipationRequirementListRequest.newBuilder()
                .setStudyId(studyId).build()
        )
    }

    override suspend fun participateInStudy(
        studyId: String,
        eligibilityTestResult: EligibilityTestResult,
        signedInformedConsent: SignedInformedConsent
    ): Result<String> = runCatching {
        studyServiceCoroutineStub.participateInStudy(
            ParticipateInStudyRequest.newBuilder().setStudyId(studyId).setEligibilityTestResult(eligibilityTestResult).setSignedInformedConsent(signedInformedConsent).build()
        ).subjectNumber
    }

    override suspend fun withdrawFromStudy(studyId: String): Result<Unit> = runCatching {
        studyServiceCoroutineStub.withdrawFromStudy(
            WithdrawFromStudyRequest.newBuilder().setStudyId(studyId).build()
        )
    }
}
