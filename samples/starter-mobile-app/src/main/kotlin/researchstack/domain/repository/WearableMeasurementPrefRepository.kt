package researchstack.domain.repository

interface WearableMeasurementPrefRepository {
    suspend fun setEcgMeasurementEnabled(enabled: Boolean): Result<Unit>
}
