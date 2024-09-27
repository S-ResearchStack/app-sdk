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
import researchstack.domain.model.events.WearablePowerState
import researchstack.domain.model.priv.PpgRed
import java.io.File

class WearablePowerStateEventRepositoryImplTest {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val wearablePowerStateEventFileRepository = FileRepository(WearablePowerState::class, context, 999L)
    private val wearablePowerStateEventRepositoryImpl =
        WearableEventRepositoryImpl(wearablePowerStateEventFileRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `test insertAll function`() {
        wearablePowerStateEventRepositoryImpl.insert(WearablePowerState())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `test insertAll function throw ClassCastException`() {
        val wearablePowerState = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            wearablePowerStateEventRepositoryImpl.insert(
                wearablePowerState as WearablePowerState
            )
        }
    }
}
