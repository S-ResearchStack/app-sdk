package researchstack.domain.usecase.wearable

import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.WearableDataReceiverRepository
import java.io.InputStream
import javax.inject.Inject

class SyncWearableDataUseCase @Inject constructor(
    private val wearableDataReceiverRepository: WearableDataReceiverRepository
) {
    suspend operator fun invoke() = wearableDataReceiverRepository.syncWearableData()

    suspend operator fun invoke(
        studyIds: List<String>,
        dataType: PrivDataType,
        csvInputStream: InputStream,
    ) = wearableDataReceiverRepository.syncWearableData(studyIds, dataType, csvInputStream)
}
