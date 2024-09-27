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
import researchstack.domain.model.events.WearablePowerState
import researchstack.domain.model.priv.PpgIr
import java.io.File
import java.io.FileNotFoundException
import java.io.InvalidObjectException
import java.nio.file.Files

class WearablePowerStateEventRepositoryTest {
    private val context = mockk<Context>()
    private val splitInterval = DateUtils.HOUR_IN_MILLIS
    private val wearablePowerStateEventFileRepository =
        spyk(FileRepository(WearablePowerState::class, context, splitInterval), recordPrivateCalls = true)

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
            wearablePowerStateEventFileRepository.deleteFile().getOrThrow()
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

        val wearablePowerState = WearablePowerState(timestamp = timeStamp)
        Assert.assertThrows(FileNotFoundException::class.java) {
            wearablePowerStateEventFileRepository.saveAll(listOf(wearablePowerState))
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

        val wearablePowerState: Timestamp = PpgIr(timestamp = timeStamp)
        Assert.assertThrows(ClassCastException::class.java) {
            wearablePowerStateEventFileRepository.saveAll(listOf(wearablePowerState as WearablePowerState))
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

        every { wearablePowerStateEventFileRepository.gson.toJson(25200000) } throws InvalidObjectException("abc")

        Assert.assertThrows(ArithmeticException::class.java) {
            wearablePowerStateEventFileRepository.saveAll(listOf(WearablePowerState(timestamp = 1 / 0L)))
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

        val wearablePowerState = WearablePowerState(timestamp = timeStamp)
        wearablePowerStateEventFileRepository.saveAll(listOf(wearablePowerState))
        Assert.assertTrue(wearablePowerStateEventFileRepository.deleteFile().isSuccess)
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
        val wearablePowerState = WearablePowerState(timestamp = timeStamp)
        wearablePowerStateEventFileRepository.saveAll(listOf(wearablePowerState))
        Assert.assertEquals(1, wearablePowerStateEventFileRepository.getCompletedFiles().size)
    }
}
