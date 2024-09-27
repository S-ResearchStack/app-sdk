package researchstack.auth.domain.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.domain.repository.IdTokenRepository
import researchstack.auth.domain.usecase.ClearIdTokenUseCase

internal class ClearIdTokenUseCaseTest {
    private val idTokenRepository = mockk<IdTokenRepository>()

    private val clearIdTokenUseCase = ClearIdTokenUseCase(idTokenRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw exception if fail to clear id-token`() = runTest {
        coEvery { idTokenRepository.clearIdToken() } throws RuntimeException()

        assertThrows<Exception> {
            clearIdTokenUseCase()
        }
    }
}
