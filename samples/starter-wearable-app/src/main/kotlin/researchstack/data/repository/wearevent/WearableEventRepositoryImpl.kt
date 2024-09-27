package researchstack.data.repository.wearevent

import researchstack.data.local.file.FileRepository
import researchstack.domain.model.Timestamp
import researchstack.domain.repository.WearableEventRepository
import java.io.File

class WearableEventRepositoryImpl<T : Timestamp>(private val fileRepository: FileRepository<T>) :
    WearableEventRepository<T> {
    override fun insertAll(data: Collection<T>) = fileRepository.saveAll(data)
    override fun insert(data: T) = fileRepository.saveAll(listOf(data))
    override fun getCompletedFiles(): List<File> = fileRepository.getCompletedFiles()
}
