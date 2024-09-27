package researchstack.domain.model.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.domain.model.task.question.RankQuestion
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag.DATETIME
import researchstack.domain.model.task.question.common.QuestionTag.RADIO
import researchstack.domain.model.task.question.common.QuestionTag.RANK
import researchstack.domain.model.task.question.common.QuestionTag.SLIDER
import researchstack.domain.model.task.question.common.QuestionTag.TEXT
import java.time.LocalDateTime

internal class TaskTest {
    private val id = 1
    private val taskId = "taskId"
    private val studyId = "studyId"
    private val title = "taskTitle"
    private val description = "Task Description"
    private val createdAt = LocalDateTime.now()
    private val scheduledAt = LocalDateTime.now()
    private val validUntil = LocalDateTime.now().plusDays(1)
    private val inClinic = true

    @Tag(POSITIVE_TEST)
    @Test
    fun `test sub-classes of task`() {
        testSurveyTask()
    }

    private val section =
        Section(
            listOf(
                ScaleQuestion("qid1", "scale", "ex", SLIDER, true, 0, 10, "low", "high"),
                DateTimeQuestion(
                    "qid2",
                    "datetime",
                    "ex",
                    DATETIME,
                    isRequired = true,
                    isDate = true,
                    isTime = true,
                    isRange = true
                ),
                ChoiceQuestion(
                    "qid3",
                    "choice",
                    "ex",
                    RADIO,
                    true,
                    listOf(Option("1", "label1"), Option("2", "label2"))
                ),
                RankQuestion(
                    "qid4",
                    "rank",
                    "ex",
                    RANK,
                    true,
                    listOf(Option("1", "label1"), Option("2", "label2"))
                ),
                TextQuestion(
                    "qid4",
                    "rank",
                    "ex",
                    TEXT,
                    true,
                ),
            )
        )

    private fun testSurveyTask() {
        val surveyTask = SurveyTask(
            id,
            taskId,
            studyId,
            title,
            description,
            createdAt,
            scheduledAt,
            validUntil,
            inClinic,
            null,
            listOf(section)
        )

        assertTask(surveyTask)
        assertEquals(1, surveyTask.sections.size)
        val actualSection = surveyTask.sections[0]
        assertEquals(section.questions.size, actualSection.questions.size)

        section.questions.forEachIndexed { index, question ->
            val actualQuestion = actualSection.questions[index]
            when (question) {
                is ScaleQuestion -> {
                    assertTrue(actualQuestion is ScaleQuestion)
                    assertScaleQuestion(question, actualQuestion as ScaleQuestion)
                }

                is DateTimeQuestion -> {
                    assertTrue(actualQuestion is DateTimeQuestion)
                    assertDateTimeQuestion(question, actualQuestion as DateTimeQuestion)
                }

                is ChoiceQuestion -> {
                    assertTrue(actualQuestion is ChoiceQuestion)
                    assertChoiceQuestion(question, actualQuestion as ChoiceQuestion)
                }

                is RankQuestion -> {
                    assertTrue(actualQuestion is RankQuestion)
                    assertRankQuestion(question, actualQuestion as RankQuestion)
                }

                is TextQuestion -> {
                    assertTrue(actualQuestion is TextQuestion)
                    assertQuestion(question, actualQuestion)
                }
            }
        }
    }

    private fun assertRankQuestion(question: RankQuestion, actualQuestion: RankQuestion) {
        assertEquals(question.options, actualQuestion.options)
        assertQuestion(question, actualQuestion)
    }

    private fun assertChoiceQuestion(question: ChoiceQuestion, actualQuestion: ChoiceQuestion) {
        assertEquals(question.options, actualQuestion.options)
        assertQuestion(question, actualQuestion)
    }

    private fun assertDateTimeQuestion(question: DateTimeQuestion, actualQuestion: DateTimeQuestion) {
        assertEquals(question.isDate, actualQuestion.isDate)
        assertEquals(question.isTime, actualQuestion.isTime)
        assertEquals(question.isRange, actualQuestion.isRange)
        assertQuestion(question, actualQuestion)
    }

    private fun assertScaleQuestion(question: ScaleQuestion, actualQuestion: ScaleQuestion) {
        assertEquals(question.high, actualQuestion.high)
        assertEquals(question.low, actualQuestion.low)
        assertEquals(question.highLabel, actualQuestion.highLabel)
        assertEquals(question.lowLabel, actualQuestion.lowLabel)
        assertQuestion(question, actualQuestion)
    }

    private fun assertQuestion(question: Question, actualQuestion: Question) {
        assertEquals(question.id, actualQuestion.id)
        assertEquals(question.title, actualQuestion.title)
        assertEquals(question.explanation, actualQuestion.explanation)
        assertEquals(question.tag, actualQuestion.tag)
        assertEquals(question.isRequired, actualQuestion.isRequired)
        assertEquals(question.hashCode(), actualQuestion.hashCode())
        assertEquals(question, actualQuestion)
    }

    private fun assertTask(task: Task) {
        assertEquals(id, task.id)
        assertEquals(taskId, task.taskId)
        assertEquals(studyId, task.studyId)
        assertEquals(title, task.title)
        assertEquals(description, task.description)
        assertEquals(createdAt, task.createdAt)
        assertEquals(scheduledAt, task.scheduledAt)
        assertEquals(validUntil, task.validUntil)
        assertNull(task.taskResult)
    }
}
