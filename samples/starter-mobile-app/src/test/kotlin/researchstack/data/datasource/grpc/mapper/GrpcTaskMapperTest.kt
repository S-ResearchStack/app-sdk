package researchstack.data.datasource.grpc.mapper

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.TaskType
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.backend.grpc.ActivityType as GrpcActivityType
import researchstack.backend.grpc.Question as GrpcQuestion
import researchstack.backend.grpc.TaskSpec as GrpcTaskSpec

class GrpcTaskMapperTest {

    private val grpcTaskSpec = mockk<GrpcTaskSpec>()
    private val grpcActivityType = mockk<GrpcActivityType>()
    private val grpcQuestion = mockk<GrpcQuestion> {
        every { id } returns "id"
        every { title } returns "title"
        every { required } returns false
        every { explanation } returns "explanation"
        every { scaleProperties.low } returns 0
        every { scaleProperties.high } returns 1
        every { choiceProperties.optionsList } returns listOf()
        every { scaleProperties.lowLabel } returns "lowLabel"
        every { scaleProperties.highLabel } returns "highLabel"
        every { dateTimeProperties.isDate } returns false
        every { dateTimeProperties.isTime } returns false
        every { dateTimeProperties.isRange } returns false
        every { rankingProperties.optionsList } returns listOf()
    }
    private val activityResult = mockk<ActivityResult> {
        val resultMap = mapOf(
            "filePath" to "testFilePath",
            "startTime" to "2011-12-03T10:15:30+00:00",
            "endTime" to "2011-12-03T10:15:30+00:00",
        )

        every { result } returns resultMap
    }
    private val questionResult = mockk<QuestionResult> {
        every { id } returns "id"
        every { result } returns "result"
    }

    private val unspecifiedSet = setOf(
        GrpcQuestion.TAG.TAG_UNSPECIFIED,
        GrpcQuestion.TAG.UNRECOGNIZED
    )
    private val todoSet = setOf(
        ActivityType.UNSPECIFIED,
        ActivityType.TAPPING_SPEED,
        ActivityType.REACTION_TIME,
        ActivityType.GUIDED_BREATHING,
        ActivityType.RANGE_OF_MOTION,
        ActivityType.GAIT_AND_BALANCE,
        ActivityType.STROOP_TEST,
        ActivityType.SPEECH_RECOGNITION,
        ActivityType.SUSTAINED_PHONATION,
        ActivityType.SHAPE_PAINTING,
        ActivityType.CATCH_LADYBUG,
        ActivityType.MEMORIZE,
        ActivityType.MEMORIZE_WORDS_START,
        ActivityType.MEMORIZE_WORDS_END,
        ActivityType.DESCRIBE_IMAGE,
        ActivityType.READ_TEXT_ALOUD,
        ActivityType.ANSWER_VERBALLY,
        ActivityType.ANSWER_WRITTEN,
    )

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcTaskSpec#getTaskType should not throw Exception`() {
        every { grpcTaskSpec.hasActivityTask() } returns true

        Assertions.assertEquals(grpcTaskSpec.getTaskType(), TaskType.ACTIVITY)

        every { grpcTaskSpec.hasActivityTask() } returns false
        every { grpcTaskSpec.hasSurveyTask() } returns true

        Assertions.assertEquals(grpcTaskSpec.getTaskType(), TaskType.SURVEY)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `getTaskType should throw Exception`() {
        every { grpcTaskSpec.hasActivityTask() } returns false
        every { grpcTaskSpec.hasSurveyTask() } returns false

        Assertions.assertThrows(IllegalStateException::class.java) {
            grpcTaskSpec.getTaskType()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcActivityType#toDomain should not throw Exception`() {
        GrpcActivityType.values().forEach {
            Assertions.assertNotNull(it.toDomain())
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcActivityType#toDomain should throw Exception`() {
        every { grpcActivityType.toDomain() } throws Exception()
        Assertions.assertThrows(Exception::class.java) {
            grpcActivityType.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcQuestion#toDomain should not throw Exception`() {
        GrpcQuestion.TAG.values().filter { !unspecifiedSet.contains(it) }.forEach {
            every { grpcQuestion.tag } returns it
            Assertions.assertNotNull(grpcQuestion.toDomain())
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcQuestion#toDomain should throw Exception`() {
        unspecifiedSet.forEach {
            every { grpcQuestion.tag } returns it
            runCatching {
                grpcQuestion.toDomain()
            }.onFailure { exception ->
                Assertions.assertTrue(exception is IllegalArgumentException)
                Assertions.assertEquals("Not supported tag.", exception.message)
            }
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `ActivityResult#toGrpcData should not throw Exception`() {
        ActivityType.values().filter { !todoSet.contains(it) }.forEach {
            every { activityResult.activityType } returns it
            Assertions.assertNotNull(activityResult.toGrpcData())
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `ActivityResult#toGrpcData should throw Exception`() {
        todoSet.forEach {
            every { activityResult.activityType } returns it
            runCatching { activityResult.toGrpcData() }.onFailure { exception ->
                Assertions.assertTrue(exception is NotImplementedError)
            }
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `QuestionResult#toGrpcData should not throw Exception`() {
        Assertions.assertNotNull(questionResult.toGrpcData())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `QuestionResult#toGrpcData should throw Exception`() {
        every { questionResult.id } throws Exception()
        Assertions.assertThrows(Exception::class.java) {
            questionResult.toGrpcData()
        }
    }
}
