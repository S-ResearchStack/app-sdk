package researchstack.data.datasource.local.pref

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST

class PrefTest {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `Context#dataStore should not throw Exception`() {
        val context = mockk<Context> {
            every { applicationContext } returns this
        }
        Assertions.assertNotNull(context.dataStore)
    }
}
