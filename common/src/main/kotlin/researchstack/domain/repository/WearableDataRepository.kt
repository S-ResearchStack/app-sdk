package researchstack.domain.repository

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.Timestamp
import java.io.File

interface WearableDataRepository<T : Timestamp> {
    fun startTracking(): Flow<T>

    suspend fun stopTracking(): Result<Unit>

    fun insertAll(data: Collection<T>)

    fun insert(data: T)

    fun deleteFile(): Result<Unit>

    fun getCompletedFiles(): List<File>
}
