package researchstack.domain.repository

interface WearableMessageSenderRepository {
    suspend fun sendLaunchAppMessage(message: String): Result<Unit>
}
