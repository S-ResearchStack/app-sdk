package researchstack.domain.usecase.file

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.FileRepository
import java.io.InputStream

internal class UploadFileUseCaseTest {
    private val fileUploadRepository = mockk<FileRepository>()

    private val uploadFileUseCase = UploadFileUseCase(fileUploadRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to upload`() = runTest {
        coEvery { fileUploadRepository.uploadImage(any(), any()) } returns Result.failure(RuntimeException())

        assertTrue(
            uploadFileUseCase("https://upload-url", "fileUrl")
                .isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to upload file`() = runTest {
        coEvery { fileUploadRepository.getPresignedUrl(any(), any()) } returns Result.success("https://upload-file.url")
        coEvery { fileUploadRepository.uploadFile(any(), any()) } returns Result.failure(RuntimeException())

        assertTrue(
            uploadFileUseCase("studyId", "fileName", InputStream.nullInputStream())
                .isFailure
        )
    }
}
