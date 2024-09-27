package researchstack.domain.usecase.study

import researchstack.domain.model.Study
import researchstack.domain.repository.UserStatusRepository
import javax.inject.Inject

class FetchStudyStatusUseCase @Inject constructor(private val userStatusRepository: UserStatusRepository) {
    suspend operator fun invoke(): List<Study> = userStatusRepository.fetchStudyStatusFromNetWork()
}
