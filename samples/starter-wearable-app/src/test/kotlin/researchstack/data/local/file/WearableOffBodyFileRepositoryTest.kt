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
import researchstack.domain.model.events.WearableOffBody
import researchstack.domain.model.priv.PpgIr
import java.io.File
import java.io.FileNotFoundException
import java.io.InvalidObjectException
import java.nio.file.Files

class WearableOffBodyFileRepositoryTest {
    private val context = mockk<Context>()
    private val splitInterval = DateUtils.HOUR_IN_MILLIS
    private val wearableOffBodyFileRepository =
        spyk(FileRepository(WearableOffBody::class, context, splitInterval), recordPrivateCalls = true)

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
            wearableOffBodyFileRepository.deleteFile().getOrThrow()
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

        val wearableOffBody = WearableOffBody(timestamp = timeStamp, isWearableOffBody = 1)
        Assert.assertThrows(FileNotFoundException::class.java) {
            wearableOffBodyFileRepository.saveAll(listOf(wearableOffBody))
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

        val wearableOffBody: Timestamp = PpgIr(timestamp = timeStamp)
        Assert.assertThrows(ClassCastException::class.java) {
            wearableOffBodyFileRepository.saveAll(listOf(wearableOffBody as WearableOffBody))
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

        every { wearableOffBodyFileRepository.gson.toJson(25200000) } throws InvalidObjectException("abc")

        Assert.assertThrows(ArithmeticException::class.java) {
            wearableOffBodyFileRepository.saveAll(listOf(WearableOffBody(timestamp = 1 / 0L, isWearableOffBody = 1)))
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

        val wearableOffBody = WearableOffBody(timestamp = timeStamp, isWearableOffBody = 1)
        wearableOffBodyFileRepository.saveAll(listOf(wearableOffBody))
        Assert.assertTrue(wearableOffBodyFileRepository.deleteFile().isSuccess)
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
        val wearableOffBody = WearableOffBody(timestamp = timeStamp, isWearableOffBody = 1)
        wearableOffBodyFileRepository.saveAll(listOf(wearableOffBody))
        Assert.assertEquals(1, wearableOffBodyFileRepository.getCompletedFiles().size)
    }
}
