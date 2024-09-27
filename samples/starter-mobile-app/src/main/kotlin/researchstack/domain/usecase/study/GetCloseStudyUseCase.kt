package researchstack.domain.usecase.study

import researchstack.domain.model.Study
import researchstack.domain.repository.StudyRepository
import javax.inject.Inject

class GetCloseStudyUseCase @Inject constructor(
    private val studyRepository: StudyRepository,
) {
    suspend operator fun invoke(studyCode: String): Result<Study> =
        studyRepository.getStudyByParticipationCode(studyCode)
}
