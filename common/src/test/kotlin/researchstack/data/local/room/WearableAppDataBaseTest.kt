package researchstack.data.local.room

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST

class WearableAppDataBaseTest {

    private lateinit var wearableAppDataBase: WearableAppDataBase
    private val context = mockk<Context>(relaxed = true)

    @BeforeEach
    fun setup() {
        every { context.applicationContext } returns context
        wearableAppDataBase = WearableAppDataBase.getDatabase(context)
    }

    @AfterEach
    fun tearDown() {
        wearableAppDataBase.close()
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `WearableAppDataBase should work fine`() {
        val instance1 = WearableAppDataBase.getDatabase(context)
        val instance2 = WearableAppDataBase.getDatabase(context)

        assertNotNull(wearableAppDataBase)
        assertNotNull(instance1)
        assertNotNull(instance2)
        assertEquals(instance1, instance2)
    }
}
