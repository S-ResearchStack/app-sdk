package researchstack.backend.integration.outport

interface StudyDataOutPort {
    suspend fun addStudyDataFile(studyId: String, filePath: String, fileName: String): Result<Unit>
}
