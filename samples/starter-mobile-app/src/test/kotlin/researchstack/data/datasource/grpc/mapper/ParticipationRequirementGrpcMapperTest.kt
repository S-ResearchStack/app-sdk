package researchstack.data.datasource.grpc.mapper

import com.google.protobuf.ProtocolStringList
import com.google.type.Date
import com.google.type.TimeOfDay
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.grpc.EligibilityTest
import researchstack.backend.grpc.EligibilityTest.Answer
import researchstack.backend.grpc.HealthData.HealthDataType
import researchstack.domain.model.shealth.SHealthDataType.UNSPECIFIED
import researchstack.domain.model.task.question.common.QuestionResult

class ParticipationRequirementGrpcMapperTest {

    private val grpcAnswer = mockk<Answer>()
    private val dateTimeAnswers = mockk<EligibilityTest.DateTimeAnswers>()
    private val date = mockk<Date>()
    private val timeOfDay = mockk<TimeOfDay>()
    private val rankingAnswers = mockk<EligibilityTest.RankingAnswers>()
    private val textAnswers = mockk<EligibilityTest.TextAnswers>()
    private val scaleAnswers = mockk<EligibilityTest.ScaleAnswers>()
    private val protocolStringList = mockk<ProtocolStringList>()
    private val questionResult = mockk<QuestionResult>()

    @BeforeEach
    fun setUp() {
        every { grpcAnswer.questionId } returns "questionId"
        every { grpcAnswer.choiceAnswers.optionsList } returns listOf()

        every { date.year } returns 2024
        every { date.month } returns 1
        every { date.day } returns 1

        every { timeOfDay.hours } returns 12
        every { timeOfDay.minutes } returns 30
        every { timeOfDay.seconds } returns 30

        every { rankingAnswers.answersList } returns protocolStringList

        every { textAnswers.answersList } returns protocolStringList

        every { scaleAnswers.from } returns 0
        every { scaleAnswers.to } returns 1

        every { dateTimeAnswers.fromDate } returns date
        every { dateTimeAnswers.toDate } returns date
        every { dateTimeAnswers.fromTime } returns timeOfDay
        every { dateTimeAnswers.toTime } returns timeOfDay

        every { grpcAnswer.dateTimeAnswers } returns dateTimeAnswers
        every { grpcAnswer.rankingAnswers } returns rankingAnswers
        every { grpcAnswer.textAnswers } returns textAnswers
        every { grpcAnswer.scaleAnswers } returns scaleAnswers

        every { questionResult.id } returns "id"
        every { questionResult.result } returns "result"
    }

    @AfterEach
    fun tearDown() {
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasChoiceAnswers should not throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns true
        Assertions.assertNotNull(grpcAnswer.toDomain())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasChoiceAnswers should throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } throws IllegalArgumentException()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            grpcAnswer.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasRankingAnswers should not throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns true
        Assertions.assertNotNull(grpcAnswer.toDomain())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasRankingAnswers should throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } throws IllegalArgumentException()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            grpcAnswer.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasScaleAnswers should not throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } returns true
        Assertions.assertNotNull(grpcAnswer.toDomain())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasScaleAnswers should throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } throws IllegalArgumentException()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            grpcAnswer.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasDateTimeAnswers should not throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } returns false
        every { grpcAnswer.hasDateTimeAnswers() } returns true
        Assertions.assertNotNull(grpcAnswer.toDomain())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasDateTimeAnswers should throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } returns false
        every { grpcAnswer.hasDateTimeAnswers() } throws IllegalArgumentException()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            grpcAnswer.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasTextAnswers should not throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } returns false
        every { grpcAnswer.hasDateTimeAnswers() } returns false
        every { grpcAnswer.hasTextAnswers() } returns true
        Assertions.assertNotNull(grpcAnswer.toDomain())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use #hasTextAnswers should throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } returns false
        every { grpcAnswer.hasDateTimeAnswers() } returns false
        every { grpcAnswer.hasTextAnswers() } throws IllegalArgumentException()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            grpcAnswer.toDomain()
        }
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcAnswer#toDomain use should throw Exception`() {
        every { grpcAnswer.hasChoiceAnswers() } returns false
        every { grpcAnswer.hasRankingAnswers() } returns false
        every { grpcAnswer.hasScaleAnswers() } returns false
        every { grpcAnswer.hasDateTimeAnswers() } returns false
        every { grpcAnswer.hasTextAnswers() } returns false
        Assertions.assertThrows(IllegalStateException::class.java) {
            grpcAnswer.toDomain()
        }
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `QuestionResult#toData use should not throw Exception`() {
        Assertions.assertNotNull(questionResult.toData())
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `GrpcHealthDataType#toDomain use should return UNSPECIFIED`() {
        listOf(HealthDataType.HEALTH_DATA_TYPE_UNSPECIFIED, HealthDataType.UNRECOGNIZED).forEach {
            Assertions.assertEquals(it.toDomain(), UNSPECIFIED)
        }
    }
}
