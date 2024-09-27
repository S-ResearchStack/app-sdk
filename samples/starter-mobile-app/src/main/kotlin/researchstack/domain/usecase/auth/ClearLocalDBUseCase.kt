package researchstack.domain.usecase.auth

import researchstack.domain.repository.LocalDBRepository
import javax.inject.Inject

class ClearLocalDBUseCase @Inject constructor(private val localDBRepository: LocalDBRepository) {
    suspend operator fun invoke() = localDBRepository.clearLocalDB()
}
