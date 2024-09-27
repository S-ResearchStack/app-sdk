package researchstack.data.local.room.entity

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST

class PassiveDataStatusEntityTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `PassiveDataStatusEntity default constructor`() {
        val dataType = "DefaultType"
        val entity = PassiveDataStatusEntity(dataType = dataType)
        assertEquals(dataType, entity.dataType)
        assertEquals(false, entity.enabled)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `PassiveDataStatusEntityCreation`() {
        val dataType = "dataType"
        val enabled = true
        val entity = PassiveDataStatusEntity(dataType, enabled)
        assertEquals(dataType, entity.dataType)
        assertEquals(enabled, entity.enabled)
    }
}
