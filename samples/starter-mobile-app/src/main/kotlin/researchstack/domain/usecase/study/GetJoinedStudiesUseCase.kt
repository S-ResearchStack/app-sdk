package researchstack.domain.usecase.study

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.Study
import researchstack.domain.repository.StudyRepository
import javax.inject.Inject

class GetJoinedStudiesUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    operator fun invoke(): Flow<List<Study>> = studyRepository.getJoinedStudies()
}
