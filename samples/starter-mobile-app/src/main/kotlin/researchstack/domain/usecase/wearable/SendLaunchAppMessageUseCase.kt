package researchstack.domain.usecase.wearable

import researchstack.domain.repository.WearableMessageSenderRepository
import javax.inject.Inject

class SendLaunchAppMessageUseCase @Inject constructor(private val wearableMessageSenderRepository: WearableMessageSenderRepository) {
    suspend operator fun invoke(message: String) =
        wearableMessageSenderRepository.sendLaunchAppMessage(message)
}
