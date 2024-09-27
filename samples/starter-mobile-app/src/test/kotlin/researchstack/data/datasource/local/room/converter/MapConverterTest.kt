package researchstack.data.datasource.local.room.converter

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST

class MapConverterTest {
    private val converter = MapConverter()

    @Test
    @Tag(NEGATIVE_TEST)
    fun `stringToMap should throw NullPointerException`() {
        Assertions.assertThrows(NullPointerException::class.java) {
            converter.stringToMap("")
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `stringToMap should return original results`() {
        val map = mapOf(
            "abc" to "abc",
            "bcn" to "def",
            "pui" to "pmh",
        )

        val json = converter.mapToString(map)
        val res = converter.stringToMap(json)

        Assertions.assertEquals(map, res)
        Assertions.assertEquals(converter.mapToString(null), "")
    }
}
