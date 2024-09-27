package researchstack.domain.usecase

import researchstack.domain.repository.SetEcgMeasurementRepository
import javax.inject.Inject

class SetEcgMeasurementEnabledUseCase @Inject constructor(
    private val setEcgMeasurementRepository: SetEcgMeasurementRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        setEcgMeasurementRepository.setEcgMeasurementEnabled(enabled)
    }
}
