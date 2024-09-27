package researchstack.data.datasource.local.room.converter

import com.google.gson.JsonSyntaxException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST

internal class StringListConverterTest {

    private val converter = StringListConverter()

    @Tag(POSITIVE_TEST)
    @Test
    fun `jsonToStringList should return converted string`() {
        val stringList = listOf("a", "b")

        assertEquals(
            stringList,
            converter.jsonToStringList(converter.stringListToJson(stringList))
        )
    }

    @Tag(NEGATIVE_TEST)
    @ParameterizedTest
    @ValueSource(strings = ["q", "qwerasdfzxcv", "qq23"])
    fun `jsonToStringList should throw IllegalStateException when not string array-string type`(param: String) {
        assertThrows<JsonSyntaxException> {
            converter.jsonToStringList(param)
        }
    }
}
