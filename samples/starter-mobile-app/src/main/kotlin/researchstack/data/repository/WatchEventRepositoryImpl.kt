package researchstack.data.repository

import researchstack.data.local.file.FileRepository
import researchstack.domain.model.Timestamp
import researchstack.domain.repository.WatchEventsRepository

class WatchEventRepositoryImpl<T : Timestamp>(private val fileRepository: FileRepository<T>) :
    WatchEventsRepository<T> {
    override fun insert(data: T) = fileRepository.saveAll(listOf(data))
}
