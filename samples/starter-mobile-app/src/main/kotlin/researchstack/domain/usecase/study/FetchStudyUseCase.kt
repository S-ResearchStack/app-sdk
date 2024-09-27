package researchstack.domain.usecase.study

import researchstack.domain.repository.StudyRepository
import javax.inject.Inject

class FetchStudyUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    suspend operator fun invoke(): Unit = studyRepository.fetchStudiesFromNetwork()
}
