package researchstack.data.datasource.local.room.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.eligibilitytest.answer.ChoiceAnswer
import researchstack.domain.model.eligibilitytest.answer.DateTimeAnswer
import researchstack.domain.model.eligibilitytest.answer.RankingAnswer
import researchstack.domain.model.eligibilitytest.answer.ScaleAnswer
import researchstack.domain.model.eligibilitytest.answer.TextAnswer
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.domain.model.shealth.SHealthDataType.UNSPECIFIED
import researchstack.domain.model.task.Section
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.domain.model.task.question.RankQuestion
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.question.common.QuestionTag.DATETIME
import researchstack.domain.model.task.question.common.QuestionTag.RADIO
import researchstack.domain.model.task.question.common.QuestionTag.RANK
import researchstack.domain.model.task.question.common.QuestionTag.SLIDER
import researchstack.domain.model.task.question.common.QuestionTag.TEXT
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

internal class EligibilityConverterTest {
    private val converter = EligibilityConverter()

    @Test
    @Tag(POSITIVE_TEST)
    fun `convertFromAnswersToJson should return original answers`() {
        val answers = listOf(
            ChoiceAnswer("q1", listOf(Option("value1", "label1"))),
            DateTimeAnswer(
                "q2",
                LocalDate.now(),
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now()
            ),
            RankingAnswer("q3", listOf("ranking1", "ranking2")),
            ScaleAnswer("q4", 1, 2),
            TextAnswer("q5", listOf("ta"))
        )

        assertEquals(
            answers,
            converter.convertFromJsonToAnswers(converter.convertFromAnswersToJson(answers))
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `convertFromJsonToSections should return original sections`() {
        val sections = listOf(
            Section(
                listOf(
                    ChoiceQuestion(
                        "q1",
                        "choice",
                        "description",
                        RADIO,
                        isRequired = true,
                        listOf(Option("v1", "l1"), Option("v2", "l2"))
                    ),
                    DateTimeQuestion(
                        "q2",
                        "datetime",
                        "description",
                        DATETIME,
                        isRequired = false,
                        isDate = true,
                        isTime = true,
                        isRange = true,
                    ),
                    RankQuestion(
                        "q3",
                        "rank",
                        "description",
                        RANK,
                        isRequired = false,
                        listOf(Option("v1", "l1"), Option("v2", "l2"))
                    ),
                    ScaleQuestion(
                        "q3",
                        "rank",
                        "description",
                        SLIDER,
                        isRequired = false,
                        low = 0,
                        high = 10,
                        lowLabel = "label1",
                        highLabel = "label2",
                    ),
                    TextQuestion(
                        "q3",
                        "rank",
                        "description",
                        TEXT,
                        isRequired = false,
                    )
                )
            )
        )

        assertEquals(
            sections,
            converter.convertFromJsonToSections(converter.convertFromSectionsToJson(sections))
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `convertFromJsonToHealthDataTypes should return original health-data-types`() {
        val sHealthDataTypes = SHealthDataType.values().filter { it != UNSPECIFIED }

        assertEquals(
            sHealthDataTypes,
            converter.convertFromJsonToHealthDataTypes(
                converter.convertFromHealthDataTypesToJson(
                    sHealthDataTypes
                )
            )

        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `convertFromJsonToSensorDataTypes should return original sensor-data-types`() {
        val trackerDataTypes = TrackerDataType.values().toList()

        assertEquals(
            trackerDataTypes,
            converter.convertFromJsonToTrackerDataTypes(
                converter.convertFromTrackerTypesToJson(
                    trackerDataTypes
                )
            )
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `convertFromJsonToSurveyResult should return original results`() {
        val surveyResult = SurveyResult(
            1,
            LocalDateTime.now(),
            LocalDateTime.now(),
            listOf(QuestionResult("q1", "survey-result"))
        )

        val actual = converter.convertFromJsonToSurveyResult(
            converter.convertFromSurveyResultsToJson(surveyResult)
        )

        assertEquals(surveyResult.questionResults, actual?.questionResults)
        assertEquals(surveyResult.id, actual?.id)
    }

    private fun Class<*>.convertFromEnumsToJson(list: List<Enum<*>>): String = when (this) {
        PrivDataType::class.java -> converter.convertFromPrivDataTypesToJson(list as List<PrivDataType>)
        DeviceStatDataType::class.java -> converter.convertFromDeviceStatDataTypesToJson(list as List<DeviceStatDataType>)
        else -> throw Exception()
    }

    private fun Class<*>.convertFromJsonToEnums(json: String): List<Enum<*>> = when (this) {
        PrivDataType::class.java -> converter.convertFromJsonToPrivDataTypes(json)
        DeviceStatDataType::class.java -> converter.convertFromJsonToDeviceStatDataTypes(json)
        else -> throw Exception()
    }

    private fun Class<*>.randomEntries(num: Int): List<Enum<*>> {
        val dataTypeList = arrayListOf<Enum<*>>()
        when (this) {
            PrivDataType::class.java -> {
                for (i in 0 until num) {
                    val index = Random.nextInt(0, PrivDataType.entries.size)
                    val item = PrivDataType.entries[index]
                    dataTypeList.add(item)
                }
            }
            DeviceStatDataType::class.java -> {
                for (i in 0 until num) {
                    val index = Random.nextInt(0, DeviceStatDataType.entries.size)
                    val item = DeviceStatDataType.entries[index]
                    dataTypeList.add(item)
                }
            }

            else -> throw Exception()
        }
        return dataTypeList
    }

    @ParameterizedTest
    @ValueSource(
        classes = [PrivDataType::class, DeviceStatDataType::class]
    )
    @Tag(POSITIVE_TEST)
    fun `convertEnumsToJson should return original results`(clazz: Class<*>) {
        val enumList = clazz.randomEntries(100)
        val res = clazz.convertFromEnumsToJson(enumList)
        val realEnums = clazz.convertFromJsonToEnums(res)

        assertEquals(realEnums, enumList)
    }

    @ParameterizedTest
    @ValueSource(
        classes = [PrivDataType::class, DeviceStatDataType::class]
    )
    @Tag(NEGATIVE_TEST)
    fun `convertJsonToEnums should throw NullPointerException`(clazz: Class<*>) {
        assertThrows(NullPointerException::class.java) {
            clazz.convertFromJsonToEnums("")
        }
    }
}
