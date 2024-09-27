package researchstack.data.local.file

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.EcgSet
import java.io.File
import java.nio.file.Files

class FileRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var fileRepository: TestFileRepository

    @BeforeEach
    fun setUp() {
        mockContext = mockk(relaxed = true)
        every { mockContext.filesDir } returns File("mockDir")
        fileRepository = TestFileRepository(mockContext)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getCompletedFiles should work fine`() {
        val file = File("build/shr_test")
        every { mockContext.filesDir } returns file
        val actualFile = fileRepository.getCompletedFiles()
        assertNotNull(actualFile)
        assertEquals(1, actualFile.size)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getCompletedFiles when listFiles() == null`() {
        val context: Context = mockk(relaxed = true)
        every { context.filesDir } returns null
        val mockFileRepository = TestFileRepository(context)
        val actualFile = mockFileRepository.getCompletedFiles()
        assertNotNull(actualFile)
        assertEquals(0, actualFile.size)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `saveAll should work fine`() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")
        val splitInterval = 3600000L

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }

        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }
        every { mockContext.filesDir } returns file
        val time = System.currentTimeMillis()

        for (i in 0..50) {
            val x = EcgSet()
            x.timestamp = time / splitInterval * splitInterval - 2 * splitInterval
            fileRepository.saveAll(listOf(x))
        }

        assertNotNull(fileRes.listFiles())
        fileRes.listFiles()?.let { assertTrue(it.isNotEmpty()) }

        val listFile = fileRepository.getCompletedFiles()
        assertNotNull(listFile)
        assertTrue(listFile.isNotEmpty())
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `deleteFile should work fine`() {
        val file = File("build/shr_test")
        every { mockContext.filesDir } returns file
        val result = fileRepository.deleteFile()
        assertTrue(result.isSuccess)
    }

    private class TestFileRepository(context: Context) : FileRepository<EcgSet>(EcgSet::class, context)
}
