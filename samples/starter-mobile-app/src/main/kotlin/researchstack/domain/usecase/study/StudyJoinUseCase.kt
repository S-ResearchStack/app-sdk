package researchstack.domain.usecase.study

import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.repository.StudyRepository
import javax.inject.Inject

class StudyJoinUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    suspend operator fun invoke(studyId: String, eligibilityTestResult: EligibilityTestResult): Result<Unit> =
        studyRepository.joinStudy(studyId, eligibilityTestResult)
}
