package researchstack.data.repository

import researchstack.data.local.pref.WearableMeasurementPref
import researchstack.domain.repository.SetEcgMeasurementRepository

class SetEcgMeasurementRepositoryImpl(
    private val wearableMeasurementPref: WearableMeasurementPref
) : SetEcgMeasurementRepository {
    override suspend fun setEcgMeasurementEnabled(enabled: Boolean) {
        wearableMeasurementPref.setEcgMeasurementEnabled(enabled)
    }
}
