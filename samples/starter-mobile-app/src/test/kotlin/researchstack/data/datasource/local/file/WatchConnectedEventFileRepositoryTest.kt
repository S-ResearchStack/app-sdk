package researchstack.data.datasource.local.file

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.events.MobileWearConnection
import java.io.File

class WatchConnectedEventFileRepositoryTest {
    private val splitInterval = 10000L
    private val context = mockk<Context>()
    private val contextFile = File("build/shr_test")
    private val watchConnectedEventFileRepository =
        spyk(FileRepository(MobileWearConnection::class, context, splitInterval))

    @BeforeEach
    fun setUp() {
        every { context.filesDir } returns contextFile
        context.filesDir.listFiles()?.forEach {
            it.deleteRecursively()
        }
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `saveAll should not throw Exception`() {
        val mobileWearConnection =
            MobileWearConnection(timestamp = System.currentTimeMillis() / splitInterval * splitInterval - splitInterval - 250)
        Assertions.assertDoesNotThrow {
            watchConnectedEventFileRepository.saveAll(
                listOf(
                    mobileWearConnection,
                    mobileWearConnection
                )
            )
        }

        context.filesDir.listFiles()?.let {
            Assertions.assertTrue(it.isNotEmpty())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `saveAll should throw Exception`() {
        Assertions.assertThrows(ArithmeticException::class.java) {
            watchConnectedEventFileRepository.saveAll(listOf(MobileWearConnection(timeOffset = 1 / 0)))
        }

        context.filesDir.listFiles()?.let {
            Assertions.assertTrue(it.isEmpty())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getCompletedFiles should not throw Exception`() {
        `saveAll should not throw Exception`()

        Assertions.assertDoesNotThrow {
            val files = watchConnectedEventFileRepository.getCompletedFiles()
            Assertions.assertTrue(files.isNotEmpty())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getCompletedFiles should return empty`() {
        contextFile.deleteRecursively()
        Assertions.assertDoesNotThrow {
            val files = watchConnectedEventFileRepository.getCompletedFiles()
            Assertions.assertTrue(files.isEmpty())
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `deleteFile should not throw Exception`() {
        `saveAll should not throw Exception`()

        Assertions.assertDoesNotThrow {
            var files = watchConnectedEventFileRepository.getCompletedFiles()
            Assertions.assertTrue(files.isNotEmpty())
            watchConnectedEventFileRepository.deleteFile()
            files = watchConnectedEventFileRepository.getCompletedFiles()
            Assertions.assertTrue(files.isEmpty())
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `deleteFile should throw Exception`() {
        contextFile.deleteRecursively()
        Assertions.assertDoesNotThrow {
            val files = watchConnectedEventFileRepository.getCompletedFiles()
            Assertions.assertTrue(files.isEmpty())
            watchConnectedEventFileRepository.deleteFile()
            Assertions.assertTrue(files.isEmpty())
        }
    }
}
