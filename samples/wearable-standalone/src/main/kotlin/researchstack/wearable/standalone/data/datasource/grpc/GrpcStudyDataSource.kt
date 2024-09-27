package researchstack.wearable.standalone.data.datasource.grpc

import io.grpc.Status
import researchstack.backend.grpc.ParticipateInStudyRequest
import researchstack.backend.grpc.StudyServiceGrpcKt.StudyServiceCoroutineStub
import researchstack.wearable.standalone.domain.exception.AlreadyJoinedStudy

class GrpcStudyDataSource(private val studyService: StudyServiceCoroutineStub) {
    suspend fun participateInStudy(
        studyId: String,
    ): Result<String> =
        runCatching {
            studyService.participateInStudy(
                ParticipateInStudyRequest.newBuilder()
                    .setStudyId(studyId)
                    .build()
            ).subjectNumber
        }.recoverCatching { ex ->
            if (Status.fromThrowable(ex).code == Status.ALREADY_EXISTS.code) throw AlreadyJoinedStudy
            throw ex
        }
}
