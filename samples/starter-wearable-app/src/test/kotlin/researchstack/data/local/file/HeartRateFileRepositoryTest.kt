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
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgIr
import java.io.File
import java.io.FileNotFoundException
import java.io.InvalidObjectException
import java.nio.file.Files

class HeartRateFileRepositoryTest {
    private val context = mockk<Context>()
    private val splitInterval = DateUtils.HOUR_IN_MILLIS
    private val heartRateFileRepository =
        spyk(FileRepository(HeartRate::class, context, splitInterval), recordPrivateCalls = true)

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
            heartRateFileRepository.deleteFile().getOrThrow()
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

        val heartRate = HeartRate(timestamp = timeStamp)
        Assert.assertThrows(FileNotFoundException::class.java) {
            heartRateFileRepository.saveAll(listOf(heartRate))
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

        val heartRate: Timestamp = PpgIr(timestamp = timeStamp)
        Assert.assertThrows(ClassCastException::class.java) {
            heartRateFileRepository.saveAll(listOf(heartRate as HeartRate))
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

        every { heartRateFileRepository.gson.toJson(25200000) } throws InvalidObjectException("abc")

        Assert.assertThrows(ArithmeticException::class.java) {
            heartRateFileRepository.saveAll(listOf(HeartRate(timestamp = 1 / 0L)))
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

        val heartRate = HeartRate(timestamp = timeStamp)
        heartRateFileRepository.saveAll(listOf(heartRate))
        Assert.assertTrue(heartRateFileRepository.deleteFile().isSuccess)
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
        val heartRate = HeartRate(timestamp = timeStamp)
        heartRateFileRepository.saveAll(listOf(heartRate))
        Assert.assertEquals(1, heartRateFileRepository.getCompletedFiles().size)
    }
}
