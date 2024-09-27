package researchstack.data.datasource.grpc

import researchstack.backend.integration.outport.FileOutPort

class GrpcFileDataSource(private val fileOutPort: FileOutPort) {
    suspend fun getPresignedUrl(fileName: String, studyId: String): Result<String> = fileOutPort.getPresignedUrl(fileName, studyId)
}
