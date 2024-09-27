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
import researchstack.domain.model.events.WearableBattery
import researchstack.domain.model.priv.PpgIr
import java.io.File
import java.io.FileNotFoundException
import java.io.InvalidObjectException
import java.nio.file.Files

class WearableBatteryFileRepositoryTest {
    private val context = mockk<Context>()
    private val splitInterval = DateUtils.HOUR_IN_MILLIS
    private val wearableBatteryFileRepository =
        spyk(FileRepository(WearableBattery::class, context, splitInterval), recordPrivateCalls = true)

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
            wearableBatteryFileRepository.deleteFile().getOrThrow()
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

        val wearableBattery = WearableBattery(timestamp = timeStamp, percentage = 10, isCharging = 1)
        Assert.assertThrows(FileNotFoundException::class.java) {
            wearableBatteryFileRepository.saveAll(listOf(wearableBattery))
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

        val wearableBattery: Timestamp = PpgIr(timestamp = timeStamp)
        Assert.assertThrows(ClassCastException::class.java) {
            wearableBatteryFileRepository.saveAll(listOf(wearableBattery as WearableBattery))
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

        every { wearableBatteryFileRepository.gson.toJson(25200000) } throws InvalidObjectException("abc")

        Assert.assertThrows(ArithmeticException::class.java) {
            wearableBatteryFileRepository.saveAll(
                listOf(
                    WearableBattery(
                        timestamp = 1 / 0L,
                        percentage = 10,
                        isCharging = 1
                    )
                )
            )
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

        val wearableBattery = WearableBattery(timestamp = timeStamp, percentage = 10, isCharging = 1)
        wearableBatteryFileRepository.saveAll(listOf(wearableBattery))
        Assert.assertTrue(wearableBatteryFileRepository.deleteFile().isSuccess)
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
        val wearableBattery = WearableBattery(timestamp = timeStamp, percentage = 10, isCharging = 1)
        wearableBatteryFileRepository.saveAll(listOf(wearableBattery))
        Assert.assertEquals(1, wearableBatteryFileRepository.getCompletedFiles().size)
    }
}
