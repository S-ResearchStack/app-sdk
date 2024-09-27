package researchstack.domain.usecase.wearable

import researchstack.domain.repository.WearableMeasurementPrefRepository
import javax.inject.Inject

class SetEcgMeasurementEnabledUseCase @Inject constructor(private val wearableMeasurementPrefRepository: WearableMeasurementPrefRepository) {
    suspend operator fun invoke(enabled: Boolean) =
        wearableMeasurementPrefRepository.setEcgMeasurementEnabled(enabled)
}
