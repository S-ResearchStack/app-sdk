package researchstack.data.repository

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.file.FileRepository
import researchstack.data.repository.wearevent.WearableEventRepositoryImpl
import researchstack.domain.model.events.WearableBattery
import researchstack.domain.model.priv.PpgRed
import java.io.File

class WearableBatteryRepositoryImplTest {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val wearableBatteryFileRepository = FileRepository(WearableBattery::class, context, 999L)
    private val wearableBatteryRepositoryImpl =
        WearableEventRepositoryImpl(wearableBatteryFileRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `test insertAll function`() {
        wearableBatteryRepositoryImpl.insertAll(
            listOf(
                WearableBattery(
                    1000,
                    10,
                    10,
                    1
                )
            )
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `test insertAll function throw ClassCastException`() {
        val wearableBattery = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            wearableBatteryRepositoryImpl.insertAll(
                listOf(
                    wearableBattery as WearableBattery
                )
            )
        }
    }
}
