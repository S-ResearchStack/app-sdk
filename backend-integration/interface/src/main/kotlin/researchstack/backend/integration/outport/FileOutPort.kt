package researchstack.backend.integration.outport

interface FileOutPort {
    suspend fun getPresignedUrl(fileName: String, studyId: String): Result<String>
}
