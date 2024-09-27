package researchstack.domain.usecase.file

import researchstack.domain.repository.FileRepository
import java.io.InputStream
import javax.inject.Inject

class SaveFileUseCase @Inject constructor(private val fileRepository: FileRepository) {
    suspend operator fun invoke(
        fileName: String,
        inputStream: InputStream,
    ): Result<Unit> = fileRepository.saveFile(fileName, inputStream)
}
