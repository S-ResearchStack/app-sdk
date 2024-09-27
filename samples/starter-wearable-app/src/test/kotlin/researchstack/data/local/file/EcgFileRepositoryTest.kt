package researchstack.data.local.file

import android.content.Context
import android.text.format.DateUtils
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.Timestamp
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.PpgIr
import java.io.File
import java.io.FileNotFoundException
import java.io.InvalidObjectException
import java.nio.file.Files

class EcgFileRepositoryTest {
    private val context = mockk<Context>()
    private val splitInterval = DateUtils.HOUR_IN_MILLIS
    private val ecgFileRepository =
        spyk(FileRepository(EcgSet::class, context, splitInterval), recordPrivateCalls = true)

    @Tag(NEGATIVE_TEST)
    @Test
    fun `delete accMeter File throw FileNotFoundException`() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }
        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }

        every { context.filesDir } throws FileNotFoundException()

        Assert.assertThrows(FileNotFoundException::class.java) {
            ecgFileRepository.deleteFile().getOrThrow()
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `save file throw FileNotFoundException `() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }
        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }
        val timeStamp = System.currentTimeMillis() / splitInterval * splitInterval - 2 * splitInterval
        every { context.filesDir } throws FileNotFoundException()

        val ecgSet = EcgSet()
        ecgSet.timestamp = timeStamp
        Assert.assertThrows(FileNotFoundException::class.java) {
            ecgFileRepository.saveAll(listOf(ecgSet))
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `save file throw ClassCastException`() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }
        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }
        val timeStamp = System.currentTimeMillis() / splitInterval * splitInterval - 2 * splitInterval
        every { context.filesDir } throws FileNotFoundException()

        val ecgSet: Timestamp = PpgIr(timestamp = timeStamp)
        Assert.assertThrows(ClassCastException::class.java) {
            ecgFileRepository.saveAll(listOf(ecgSet as EcgSet))
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `save file throw ArithmeticException`() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }
        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }
        every { context.filesDir } returns file

        every { ecgFileRepository.gson.toJson(25200000) } throws InvalidObjectException("abc")

        Assert.assertThrows(ArithmeticException::class.java) {
            val ecgSet = EcgSet()
            ecgSet.timestamp = 1 / 0L
            ecgFileRepository.saveAll(listOf(ecgSet))
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `delete accMeter File after sync file to mobile`() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }
        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }
        val timeStamp = System.currentTimeMillis() / splitInterval * splitInterval - 2 * splitInterval
        every { context.filesDir } returns file

        val ecgSet = EcgSet()
        ecgSet.timestamp = timeStamp
        ecgFileRepository.saveAll(listOf(ecgSet))
        Assert.assertTrue(ecgFileRepository.deleteFile().isSuccess)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `get completed files should return list with size greater than 0`() {
        val file = File("build/shr_test")
        val fileRes = File(file.path + "/health_data")

        fileRes.listFiles()?.forEach {
            it.deleteRecursively()
        }

        if (!file.exists()) {
            Files.createDirectory(file.toPath())
        }
        every { context.filesDir } returns file
        val timeStamp = System.currentTimeMillis() / splitInterval * splitInterval - 2 * splitInterval
        val ecgSet = EcgSet()
        ecgSet.timestamp = timeStamp
        ecgFileRepository.saveAll(listOf(ecgSet))
        Assert.assertEquals(1, ecgFileRepository.getCompletedFiles().size)
    }
}
