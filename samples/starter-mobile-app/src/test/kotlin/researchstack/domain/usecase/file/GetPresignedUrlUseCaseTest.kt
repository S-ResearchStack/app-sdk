package researchstack.domain.usecase.file

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.FileRepository

internal class GetPresignedUrlUseCaseTest {
    private val fileUploadRepository = mockk<FileRepository>()

    private val getPresignedUrlUseCase = GetPresignedUrlUseCase(fileUploadRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to get signed-url`() = runTest {
        coEvery { fileUploadRepository.getPresignedUrl(any(), any()) } returns Result.failure(RuntimeException())

        assertTrue(
            getPresignedUrlUseCase("filename", "studyId").isFailure
        )
    }
}
