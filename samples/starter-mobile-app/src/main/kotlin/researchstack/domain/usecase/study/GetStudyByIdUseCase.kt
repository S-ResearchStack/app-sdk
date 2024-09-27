package researchstack.domain.usecase.study

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.Study
import researchstack.domain.repository.StudyRepository
import javax.inject.Inject

class GetStudyByIdUseCase @Inject constructor(private val studyRepository: StudyRepository) {
    operator fun invoke(studyId: String): Flow<Study> =
        studyRepository.getStudyById(studyId)
}
