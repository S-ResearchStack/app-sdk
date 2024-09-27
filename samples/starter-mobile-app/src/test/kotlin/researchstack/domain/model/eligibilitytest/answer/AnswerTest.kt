package researchstack.domain.model.eligibilitytest.answer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionType
import researchstack.domain.model.task.question.common.QuestionType.CHOICE
import java.time.LocalDate
import java.time.LocalTime

internal class AnswerTest {

    private val questionId = "questionId"

    @Test
    @Tag(POSITIVE_TEST)
    fun `test subclasses of Answer`() {
        testChoiceAnswer()
        testRankingAnswer()
        testDateTimeAnswer()
        testScaleAnswer()
        testTextAnswer()
    }

    private fun testTextAnswer() {
        val answers = listOf("t", "a")
        val textAnswer = TextAnswer(questionId, answers)
        assertEquals(QuestionType.TEXT, textAnswer.type)
        assertEquals(answers, textAnswer.answers)
        assertEquals(textAnswer.hashCode(), TextAnswer(questionId, answers).hashCode())
        assertEquals(textAnswer, TextAnswer(questionId, answers))
    }

    private fun testScaleAnswer() {
        val from = 0
        val to = 10
        val scaleAnswer = ScaleAnswer(questionId, from, to)

        assertEquals(QuestionType.SCALE, scaleAnswer.type)
        assertEquals(from, scaleAnswer.from)
        assertEquals(to, scaleAnswer.to)
        assertEquals(scaleAnswer.hashCode(), ScaleAnswer(questionId, from, to).hashCode())
        assertEquals(scaleAnswer, ScaleAnswer(questionId, from, to))
    }

    private fun testDateTimeAnswer() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        val dtAnswer = DateTimeAnswer(questionId, date, date, time, time)

        assertEquals(QuestionType.DATETIME, dtAnswer.type)
        assertEquals(date, dtAnswer.fromDate)
        assertEquals(date, dtAnswer.toDate)
        assertEquals(time, dtAnswer.fromTime)
        assertEquals(time, dtAnswer.toTime)
        assertEquals(dtAnswer.hashCode(), DateTimeAnswer(questionId, date, date, time, time).hashCode())
        assertEquals(
            dtAnswer,
            DateTimeAnswer(questionId, date, date, time, time)
        )
    }

    private fun testRankingAnswer() {
        val answers = listOf("a", "b")
        val rankingAnswer = RankingAnswer(
            questionId,
            answers
        )

        assertEquals(questionId, rankingAnswer.questionId)
        assertEquals(answers, rankingAnswer.answers)
        assertEquals(QuestionType.RANK, rankingAnswer.type)
        assertEquals(rankingAnswer.hashCode(), RankingAnswer(questionId, answers).hashCode())
        assertNotEquals(rankingAnswer, RankingAnswer("questionId", listOf("c", "d")))
    }

    private fun testChoiceAnswer() {
        val options = listOf(Option("1", "label"))
        val choiceAnswer = ChoiceAnswer(
            questionId,
            options
        )

        assertEquals(questionId, choiceAnswer.questionId)
        assertEquals(options, choiceAnswer.options)
        assertEquals(CHOICE, choiceAnswer.type)
        assertEquals(choiceAnswer.hashCode(), ChoiceAnswer(questionId, options).hashCode())
        assertNotEquals(choiceAnswer, ChoiceAnswer("other-id", options))
    }
}
