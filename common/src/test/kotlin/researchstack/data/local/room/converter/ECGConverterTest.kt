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
import researchstack.domain.model.priv.Ecg
import researchstack.domain.model.priv.PpgGreen

class ECGConverterTest {

    private lateinit var ecgConverter: ECGConverter
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        gson = Gson()
        ecgConverter = ECGConverter()
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `convertFromEcgsToJson should return json string from ecgs`() {
        val ecgs = listOf(Ecg(0, 0.0F), Ecg(1, 1.0F))
        val expectedJson = gson.toJson(ecgs)
        val actualJson = ecgConverter.convertFromEcgsToJson(ecgs)

        assertEquals(expectedJson, actualJson)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `convertFromEcgsToJson should throw exception when Ecg list is null`() {
        val ecgs: List<Ecg>? = null
        assertThrows<NullPointerException> {
            ecgConverter.convertFromEcgsToJson(ecgs!!)
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `convertFromJsonToEcgs should return Ecg list from JSON string`() {
        val ecgs = listOf(Ecg(0, 0.0F), Ecg(1, 1.0F))
        val json = gson.toJson(ecgs)
        val actualEcgs = ecgConverter.convertFromJsonToEcgs(json)

        assertEquals(ecgs, actualEcgs)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `convertFromJsonToEcgs should throw exception when JSON is invalid`() {
        assertThrows<JsonSyntaxException> {
            ecgConverter.convertFromJsonToEcgs("invalid json string")
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `convertFromPpgGreensToJson should return JSON string from list of PPG Green`() {
        val ppgGreens = listOf(PpgGreen(0, 0), PpgGreen(1, 1))
        val expectedJson = gson.toJson(ppgGreens)
        val actualJson = ecgConverter.convertFromPpgGreensToJson(ppgGreens)

        assertEquals(expectedJson, actualJson)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `convertFromPpgGreensToJson should throw exception when list of PPG Green is null`() {
        val ppgGreens: List<PpgGreen>? = null
        assertThrows<NullPointerException> {
            ecgConverter.convertFromPpgGreensToJson(ppgGreens!!)
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `convertFromJsonToPpgGreens should return list of PPG Green from JSON string`() {
        val ppgGreens = listOf(PpgGreen(0, 0), PpgGreen(1, 1))
        val json = gson.toJson(ppgGreens)
        val actualPpgGreens = ecgConverter.convertFromJsonToPpgGreens(json)

        assertEquals(ppgGreens, actualPpgGreens)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `convertFromJsonToPpgGreens should throw exception when JSON string is invalid`() {
        assertThrows<JsonSyntaxException> {
            ecgConverter.convertFromJsonToPpgGreens("invalid json string")
        }
    }
}
