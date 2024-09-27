package researchstack.backend.integration.adapter.outgoing

import researchstack.backend.grpc.FileServiceGrpcKt
import researchstack.backend.grpc.GetPresignedUrlRequest
import researchstack.backend.integration.outport.FileOutPort
import javax.inject.Inject

class FileAdapter @Inject constructor(
    private val fileServiceCoroutineStub: FileServiceGrpcKt.FileServiceCoroutineStub
) : FileOutPort {
    override suspend fun getPresignedUrl(fileName: String, studyId: String): Result<String> = kotlin.runCatching {
        fileServiceCoroutineStub.getPresignedUrl(GetPresignedUrlRequest.newBuilder().setFileName(fileName).setStudyId(studyId).build()).presignedUrl
    }
}
