package researchstack.domain.repository

import researchstack.domain.model.priv.PrivDataType
import java.io.File

interface DataSenderRepository {
    suspend fun sendData(serializableData: Any, privDataType: PrivDataType): Result<Unit>

    suspend fun sendFile(file: File, onResult: (isSuccess: Boolean) -> Unit): Result<Unit>

    suspend fun isConnected(): Boolean
}
