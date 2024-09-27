package researchstack.domain.usecase.wearable

import com.google.gson.JsonObject
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.WearableDataReceiverRepository
import java.io.InputStream
import javax.inject.Inject

class SaveWearableDataUseCase @Inject constructor(
    private val wearableDataReceiverRepository: WearableDataReceiverRepository
) {
    operator fun invoke(jsonObject: JsonObject) = wearableDataReceiverRepository.saveWearableData(jsonObject)

    operator fun invoke(dataType: PrivDataType, csvInputStream: InputStream) =
        wearableDataReceiverRepository.saveWearableData(dataType, csvInputStream)
}
