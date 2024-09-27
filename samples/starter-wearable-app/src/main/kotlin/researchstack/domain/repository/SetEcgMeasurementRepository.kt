package researchstack.domain.repository

interface SetEcgMeasurementRepository {
    suspend fun setEcgMeasurementEnabled(enabled: Boolean)
}
