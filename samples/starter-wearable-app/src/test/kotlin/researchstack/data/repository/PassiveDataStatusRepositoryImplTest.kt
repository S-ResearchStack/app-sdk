package researchstack.data.repository

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.data.local.room.entity.PassiveDataStatusEntity
import researchstack.domain.model.priv.PrivDataType

class PassiveDataStatusRepositoryImplTest {
    private val passiveDataStatusDao = mockk<PassiveDataStatusDao> {
        coEvery { save(any()) } returns Unit
        every { getEnabledData() } returns flowOf(listOf(PassiveDataStatusEntity("abc", true)))
    }
    private val passiveDataStatusRepositoryImpl =
        PassiveDataStatusRepositoryImpl(passiveDataStatusDao)

    @Test
    @Tag(POSITIVE_TEST)
    fun `set status and get status for passive data status should be success`() = runTest {
        passiveDataStatusRepositoryImpl.setStatus(PrivDataType.WEAR_PPG_GREEN, true)
        passiveDataStatusRepositoryImpl.setStatus(PrivDataType.WEAR_HEART_RATE, true)
        val result = passiveDataStatusRepositoryImpl.getStatus()
        Assert.assertNotNull(result)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `get status for passive data status should return null`() = runTest {
        this.backgroundScope.launch {
            passiveDataStatusRepositoryImpl.getStatus().collect {
                Assertions.assertNull(it)
            }
        }
    }
}
