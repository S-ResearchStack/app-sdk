package researchstack.wearable.standalone.domain.usecase.study

import researchstack.wearable.standalone.domain.repository.StudyRepository
import javax.inject.Inject

class StudyJoinUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    suspend operator fun invoke(studyId: String): Result<Unit> =
        studyRepository.joinStudy(studyId)
}
