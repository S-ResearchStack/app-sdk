package researchstack.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.backend.integration.outport.StudyDataOutPort
import researchstack.domain.repository.StudyDataRepository

class StudyDataRepositoryImpl(
    private val studyDataOutPort: StudyDataOutPort
) : StudyDataRepository {
    override suspend fun sendSignalForUploadingStudyDataFile(studyId: String, filePath: String, fileName: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            studyDataOutPort.addStudyDataFile(studyId, filePath, fileName)
        }
}
