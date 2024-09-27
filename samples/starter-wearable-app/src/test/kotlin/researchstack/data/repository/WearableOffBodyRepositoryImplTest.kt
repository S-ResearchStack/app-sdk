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
import researchstack.domain.model.events.WearableOffBody
import researchstack.domain.model.priv.PpgRed
import java.io.File

class WearableOffBodyRepositoryImplTest {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val wearableOffBodyFileRepository = FileRepository(WearableOffBody::class, context, 999L)
    private val wearableOffBodyRepositoryImpl =
        WearableEventRepositoryImpl(wearableOffBodyFileRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `test insertAll function`() {
        wearableOffBodyRepositoryImpl.insertAll(
            listOf(
                WearableOffBody(
                    1000,
                    10,
                    1
                )
            )
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `test insertAll function throw ClassCastException`() {
        val wearableOffBody = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            wearableOffBodyRepositoryImpl.insertAll(
                listOf(
                    wearableOffBody as WearableOffBody
                )
            )
        }
    }
}
