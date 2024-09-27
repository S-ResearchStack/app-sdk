package researchstack.domain.repository

interface WearablePassiveDataStatusSenderRepository {
    suspend fun sendPassiveDataStatus(passiveDataType: Enum<*>, status: Boolean): Result<Unit>
}
