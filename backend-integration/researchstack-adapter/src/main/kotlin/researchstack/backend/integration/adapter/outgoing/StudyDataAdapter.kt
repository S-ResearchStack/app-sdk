package researchstack.backend.integration.adapter.outgoing

import researchstack.backend.grpc.AddStudyDataFileRequest
import researchstack.backend.grpc.StudyDataServiceGrpcKt
import researchstack.backend.integration.outport.StudyDataOutPort
import javax.inject.Inject

class StudyDataAdapter @Inject constructor(
    private val studyDataServiceCoroutineStub: StudyDataServiceGrpcKt.StudyDataServiceCoroutineStub
) : StudyDataOutPort {
    override suspend fun addStudyDataFile(studyId: String, filePath: String, fileName: String): Result<Unit> = runCatching {
        studyDataServiceCoroutineStub.addStudyDataFile(
            AddStudyDataFileRequest.newBuilder().apply {
                this.studyId = studyId
                this.filePath = filePath
                this.fileName = fileName
            }.build()
        )
    }
}
