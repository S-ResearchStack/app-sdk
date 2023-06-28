package healthstack.app.task.entity

import healthstack.app.task.entity.Task.Properties
import healthstack.backend.integration.task.ChoiceProperties
import healthstack.backend.integration.task.Contents
import healthstack.backend.integration.task.DateTimeProperties
import healthstack.backend.integration.task.Item
import healthstack.backend.integration.task.Option
import healthstack.backend.integration.task.RankingProperties
import healthstack.backend.integration.task.ScaleProperties
import healthstack.backend.integration.task.TextProperties
import healthstack.kit.task.survey.SurveyTask
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class TaskTest {

    private val task = Task(
        id = 1,
        revisionId = 1,
        taskId = "task-id",
        properties = Properties(
            title = "property-title",
            description = "property description",
            items = emptyList()
        ),
        type = "SURVEY",
        scheduledAt = LocalDateTime.now(),
        validUntil = LocalDateTime.now().plusDays(1)
    )

    private val scaleContent = Contents(
        type = SCALE,
        title = "SCALE Question",
        itemProperties = ScaleProperties(
            tag = "slider",
            low = 0,
            high = 0,
            lowLabel = "lowest value",
            highLabel = "highers value",
        ),
        required = true,
    )

    private val textContent = Contents(
        type = TEXT,
        title = "Text Question",
        itemProperties = TextProperties(
            tag = "text",
        ),
        required = true,
    )

    private val dateTimeContent = Contents(
        type = DATETIME,
        title = "DateTime Question",
        itemProperties = DateTimeProperties(
            tag = "dateTime",
            isDate = true,
            isTime = true,
            isRange = true,
        ),
        required = true,
    )

    private val rankingContent = Contents(
        type = RANK,
        title = "Rank Question",
        itemProperties = RankingProperties(
            tag = "text",
            options = listOf(
                Option("Choice 1"),
                Option("Choice 2")
            )
        ),
        required = true,
    )

    private val choiceContent = Contents(
        type = CHOICE,
        title = "choice Question",
        itemProperties = ChoiceProperties(
            tag = "checkbox",
            options = listOf(
                Option("Choice 1"),
                Option("Choice 2")
            )
        ),
        required = true,
    )

    @Tag("negative")
    @Test
    fun `toViewTask should throw NotImplementedError when type of given item is not supported`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "invaild",
                sequence = 0,
                contents = scaleContent.copy(type = "invalid")
            )
        )

        assertThrows<NotImplementedError> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Choice and content is Scale`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = scaleContent.copy(type = CHOICE)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Choice and content is Text`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = textContent.copy(type = CHOICE)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Choice and content is Rank`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = rankingContent.copy(type = CHOICE)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Choice and content is DateTime`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = dateTimeContent.copy(type = CHOICE)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is DateTime and content is Choice`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = choiceContent.copy(type = DATETIME)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is DateTime and content is Text`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = textContent.copy(type = DATETIME)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is DateTime and content is Rank`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = rankingContent.copy(type = DATETIME)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is DateTime and content is Scale`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = scaleContent.copy(type = DATETIME)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Text and content is Scale`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = scaleContent.copy(type = TEXT)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Text and content is Choice`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = choiceContent.copy(type = TEXT)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Text and content is DateTime`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = dateTimeContent.copy(type = TEXT)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Text and content is Ranking`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = rankingContent.copy(type = TEXT)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Ranking and content is Text`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = "QUESTION",
                sequence = 0,
                contents = textContent.copy(type = RANK)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("negative")
    @Test
    fun `toViewTask should throw IllegalArgumentException when type is Scale and content is Choice`() {
        val testTask = task.changeItem(
            Item(
                name = "test-item",
                type = SCALE,
                sequence = 0,
                contents = choiceContent.copy(type = SCALE)
            )
        )

        assertThrows<IllegalArgumentException> {
            testTask.toViewTask()
        }
    }

    @Tag("positive")
    @Test
    fun `toViewTask should return survey task`() {
        val testTask = task.changeItems(
            listOf(
                Item(
                    name = "test-item-1",
                    type = SCALE,
                    sequence = 0,
                    contents = scaleContent
                ),
                Item(
                    name = "test-item-2",
                    type = CHOICE,
                    sequence = 0,
                    contents = choiceContent
                ),
            )
        )

        val surveyTask = testTask.toViewTask() as SurveyTask
        val questions = surveyTask.step.subStepHolder.subSteps.first().map { it.model }
        assertEquals(testTask.properties.items.size, questions.size)
        assertTrue(questions[0] is ChoiceQuestionModel)
        assertTrue(questions[1] is MultiChoiceQuestionModel)
    }

    private fun Task.changeItem(item: Item): Task =
        this.changeItems(listOf(item))

    private fun Task.changeItems(items: List<Item>): Task =
        this.copy(
            properties = this.properties.copy(
                items = items
            )
        )
}
