package researchstack.domain.repository

import researchstack.domain.model.Timestamp
import java.io.File

interface WearableEventRepository<T : Timestamp> {
    fun insertAll(data: Collection<T>)
    fun insert(data: T)
    fun getCompletedFiles(): List<File>
}
