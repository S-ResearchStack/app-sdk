package researchstack.domain.usecase.study

import kotlinx.coroutines.flow.first
import researchstack.domain.repository.StudyRepository
import javax.inject.Inject

class WithdrawFromAllStudyUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    suspend operator fun invoke() {
        studyRepository.getJoinedStudies().first().forEach { study ->
            studyRepository.withdrawFromStudy(study.id)
        }
    }
}
