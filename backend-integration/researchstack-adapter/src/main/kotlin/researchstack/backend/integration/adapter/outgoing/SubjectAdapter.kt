package researchstack.backend.integration.adapter.outgoing

import com.google.protobuf.Empty
import researchstack.backend.grpc.GetSubjectStatusRequest
import researchstack.backend.grpc.RegisterSubjectRequest
import researchstack.backend.grpc.SubjectGrpcKt
import researchstack.backend.grpc.SubjectProfile
import researchstack.backend.grpc.SubjectStatus
import researchstack.backend.grpc.UpdateSubjectProfileRequest
import researchstack.backend.integration.outport.SubjectOutPort
import javax.inject.Inject

class SubjectAdapter @Inject constructor(
    private val subjectServiceCoroutineStub: SubjectGrpcKt.SubjectCoroutineStub
) : SubjectOutPort {
    override suspend fun registerSubject(subjectProfile: SubjectProfile): Result<Unit> = runCatching {
            subjectServiceCoroutineStub.registerSubject(
                RegisterSubjectRequest.newBuilder().setSubjectProfile(subjectProfile)
                    .build()
            )
    }

    override suspend fun getSubjectProfile(): Result<SubjectProfile> = runCatching {
        subjectServiceCoroutineStub.getSubjectProfile(Empty.getDefaultInstance()).subjectProfile
    }

    override suspend fun updateSubjectProfile(subjectProfile: SubjectProfile): Result<Unit> = runCatching {
        subjectServiceCoroutineStub.updateSubjectProfile(
            UpdateSubjectProfileRequest.newBuilder().setSubjectProfile(subjectProfile)
                .build()
        )
    }

    override suspend fun deregisterSubject(): Result<Unit> = runCatching {
        subjectServiceCoroutineStub.deregisterSubject(Empty.getDefaultInstance())
    }

    override suspend fun getSubjectStatus(studyId: String): Result<SubjectStatus> = runCatching {
        subjectServiceCoroutineStub.getSubjectStatus(
            GetSubjectStatusRequest.newBuilder().setStudyId(studyId).build()
        ).subjectStatus
    }
}
