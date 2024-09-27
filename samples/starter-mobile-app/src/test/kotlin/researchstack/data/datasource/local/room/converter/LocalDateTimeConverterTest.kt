package researchstack.data.datasource.local.room.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import java.time.LocalDateTime

internal class LocalDateTimeConverterTest {
    private val localDateTimeConverter = LocalDateTimeConverter()

    @Tag(POSITIVE_TEST)
    @Test
    fun `timeToString should return converted string`() {
        val localDateTime = LocalDateTime.now()
        assertEquals(
            localDateTime.toString(),
            localDateTimeConverter.timeToString(localDateTime)
        )
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `timeToString should return null when null is passed`() {
        assertNull(
            localDateTimeConverter.timeToString(null)
        )
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `stringToTime should return LocalDateTime`() {
        val localDateTimeString = LocalDateTime.now().toString()
        assertEquals(
            LocalDateTime.parse(localDateTimeString),
            localDateTimeConverter.stringToTime(localDateTimeString)
        )
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `stringToTime should return null when null is passed`() {
        assertNull(
            localDateTimeConverter.stringToTime(null)
        )
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `stringToTime should return null when value is not LocalDateTime format`() {
        assertNull(
            localDateTimeConverter.stringToTime("invalid-format")
        )
    }
}
