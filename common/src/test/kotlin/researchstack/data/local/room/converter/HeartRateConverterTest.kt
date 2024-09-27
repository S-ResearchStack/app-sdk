package researchstack.data.local.room.converter

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.assertThrows
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST

class HeartRateConverterTest {
    private lateinit var heartRateConverter: HeartRateConverter
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        heartRateConverter = HeartRateConverter()
        gson = Gson()
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `convertFromListToJson should return json string`() {
        val list = listOf(1, 2, 3, 4)
        val expectedJson = gson.toJson(list)
        val actualJson = heartRateConverter.convertFromListToJson(list)
        assertEquals(expectedJson, actualJson)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `convertFromListToJson should throw exception when list is null`() {
        val ibiList: List<Int>? = null
        assertThrows<NullPointerException> {
            heartRateConverter.convertFromListToJson(ibiList!!)
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `convertFromJsonToList should return list of int`() {
        val json = "[1,2,3,4]"
        val expectedList = listOf(1, 2, 3, 4)
        val actualList = heartRateConverter.convertFromJsonToList(json)
        assertEquals(expectedList, actualList)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `convertFromJsonToList should throw exception when json is invalid`() {
        assertThrows<JsonSyntaxException> {
            heartRateConverter.convertFromJsonToList("invalid json string")
        }
    }
}
