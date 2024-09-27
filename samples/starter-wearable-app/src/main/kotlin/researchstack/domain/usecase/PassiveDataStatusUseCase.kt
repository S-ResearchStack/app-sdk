package researchstack.domain.usecase

import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.PassiveDataStatusRepository
import javax.inject.Inject

class PassiveDataStatusUseCase @Inject constructor(private val passiveDataStatusRepository: PassiveDataStatusRepository) {
    operator fun invoke() =
        passiveDataStatusRepository.getStatus()

    suspend operator fun invoke(dataType: PrivDataType, status: Boolean) =
        passiveDataStatusRepository.setStatus(dataType, status)
}
