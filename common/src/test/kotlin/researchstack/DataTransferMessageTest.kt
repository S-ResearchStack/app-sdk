package researchstack

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.domain.model.priv.PrivDataType

class DataTransferMessageTest {
    private val dataType = PrivDataType.WEAR_SPO2
    private val data = "Test data"

    @Tag(POSITIVE_TEST)
    @Test
    fun `DataTransferMessage should be initialized correctly`() {
        val message = DataTransferMessage(dataType, data)
        assertEquals(dataType, message.dataType)
        assertEquals(data, message.data)
    }
}
