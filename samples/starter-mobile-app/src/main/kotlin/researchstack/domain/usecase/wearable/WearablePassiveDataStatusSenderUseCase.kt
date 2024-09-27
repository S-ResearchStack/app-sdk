package researchstack.domain.usecase.wearable

import researchstack.domain.repository.WearablePassiveDataStatusSenderRepository
import javax.inject.Inject

class WearablePassiveDataStatusSenderUseCase @Inject constructor(
    private val wearablePassiveDataStatusSenderRepository: WearablePassiveDataStatusSenderRepository
) {
    suspend operator fun invoke(
        passiveDataType: Enum<*>,
        status: Boolean
    ) = wearablePassiveDataStatusSenderRepository.sendPassiveDataStatus(passiveDataType, status)
}
