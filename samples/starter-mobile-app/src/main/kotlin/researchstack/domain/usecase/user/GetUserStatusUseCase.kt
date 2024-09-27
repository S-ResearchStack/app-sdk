package researchstack.domain.usecase.user

import researchstack.domain.model.StudyStatusModel
import researchstack.domain.repository.UserStatusRepository
import javax.inject.Inject

class GetUserStatusUseCase @Inject constructor(private val userStatusRepository: UserStatusRepository) {
    suspend operator fun invoke(studyId: String): Result<StudyStatusModel> =
        userStatusRepository.getStudyStatusById(studyId)
}
