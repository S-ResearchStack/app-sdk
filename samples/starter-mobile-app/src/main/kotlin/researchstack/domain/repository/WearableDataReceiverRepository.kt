package researchstack.domain.repository

import com.google.gson.JsonObject
import researchstack.domain.model.priv.PrivDataType
import java.io.InputStream

interface WearableDataReceiverRepository {
    fun saveWearableData(jsonObject: JsonObject)

    fun saveWearableData(dataType: PrivDataType, csvInputStream: InputStream)

    suspend fun syncWearableData(
        studyIds: List<String>,
        dataType: PrivDataType,
        csvInputStream: InputStream,
    )

    suspend fun syncWearableData()
}
