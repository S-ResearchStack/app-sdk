package researchstack.domain.repository

interface StudyDataRepository {
    suspend fun sendSignalForUploadingStudyDataFile(studyId: String, filePath: String, fileName: String): Result<Unit>
}
