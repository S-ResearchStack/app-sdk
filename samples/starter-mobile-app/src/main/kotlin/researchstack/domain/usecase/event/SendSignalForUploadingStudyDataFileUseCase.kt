package researchstack.domain.usecase.event

import researchstack.domain.repository.StudyDataRepository
import javax.inject.Inject

class SendSignalForUploadingStudyDataFileUseCase @Inject constructor(private val studyDataRepository: StudyDataRepository) {
    suspend operator fun invoke(studyId: String, filePath: String, fileName: String): Result<Unit> = studyDataRepository.sendSignalForUploadingStudyDataFile(studyId, filePath, fileName)
}
