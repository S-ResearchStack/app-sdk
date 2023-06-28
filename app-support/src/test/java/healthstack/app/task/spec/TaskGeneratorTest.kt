package healthstack.app.task.spec

import healthstack.backend.integration.task.ChoiceProperties
import healthstack.backend.integration.task.Contents
import healthstack.backend.integration.task.Item
import healthstack.backend.integration.task.ItemProperties
import healthstack.backend.integration.task.Option
import healthstack.backend.integration.task.ScaleProperties
import healthstack.backend.integration.task.TaskSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.ParseException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class TaskGeneratorTest {

    private val startTime = LocalDateTime.now()
    private val endTime = startTime.plusDays(6)
    private val everydayTaskSpec = TaskSpec(
        revisionId = 1,
        taskId = "task-id",
        type = "SURVEY",
        title = "spec-test",
        description = "good test",
        schedule = "0 0 12 1/1 * ? *",
        startTime = startTime.toString(),
        endTime = endTime.toString(),
        validTime = 600L,
        items = listOf(
            Item(
                name = "q1",
                type = "QUESTION",
                sequence = 0,
                contents = Contents(
                    type = "SCALE",
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
            ),
            Item(
                name = "q2",
                type = "QUESTION",
                sequence = 0,
                contents = Contents(
                    type = "CHOICE",
                    title = "choice Question",
                    itemProperties = ChoiceProperties(
                        tag = "radio",
                        options = listOf(
                            Option("Choice 1"),
                            Option("Choice 2")
                        )
                    ),
                    required = true,
                )
            )
        )
    )

    @Tag("positive")
    @Test
    fun `generate should return tasks with given spec`() {
        val tasks = TaskGenerator.generate(everydayTaskSpec)

        assertEquals(ChronoUnit.DAYS.between(startTime, endTime), tasks.size.toLong())
        tasks.forEach { task ->
            assertEquals(everydayTaskSpec.taskId, task.taskId)
            assertEquals(everydayTaskSpec.revisionId, task.revisionId)
            assertEquals(everydayTaskSpec.title, task.properties.title)
            assertEquals(everydayTaskSpec.description, task.properties.description)

            assertNotNull(task.scheduledAt)
            assertEquals(task.scheduledAt.plusMinutes(everydayTaskSpec.validTime), task.validUntil)
            assertEquals(everydayTaskSpec.items.size, task.properties.items.size)
            everydayTaskSpec.items.forEachIndexed { index, item ->
                assertEquals(item, task.properties.items[index])
            }
        }
    }

    private fun assertEquals(expected: Item, actual: Item) {
        assertEquals(expected.name, actual.name)
        assertEquals(expected.type, actual.type)
        assertEquals(expected.sequence, actual.sequence)
        assertEquals(expected.contents, actual.contents)
    }

    private fun assertEquals(expected: Contents, actual: Contents) {
        assertEquals(expected.type, actual.type)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.explanation, actual.explanation)
        assertEquals(expected.required, actual.required)
        assertEquals(expected.itemProperties, actual.itemProperties)
    }

    private fun assertEquals(expected: ItemProperties, actual: ItemProperties) {
        assertEquals(expected.tag, actual.tag)

        if (expected is ChoiceProperties) {
            assertEquals(expected.options, (actual as? ChoiceProperties)?.options)
        } else if (expected is ScaleProperties) {
            val actualProps = (actual as? ScaleProperties)
            assertEquals(expected.high, actualProps?.high)
            assertEquals(expected.low, actualProps?.low)
            assertEquals(expected.highLabel, actualProps?.highLabel)
            assertEquals(expected.lowLabel, actualProps?.lowLabel)
        }
    }

    @Tag("negative")
    @Test
    fun `generate should throw ParseException when schedule is invalid cron expression`() {
        assertThrows<ParseException> {
            TaskGenerator.generate(everydayTaskSpec.copy(schedule = "invalid cron expression"))
        }
    }

    @Tag("negative")
    @Test
    fun `generate should throw InvalidArgument when validTime is negative`() {
        assertThrows<IllegalArgumentException> {
            TaskGenerator.generate(everydayTaskSpec.copy(validTime = -1L))
        }
    }

    @Tag("negative")
    @Test
    fun `generate should throw InvalidArgument when title is empty`() {
        assertThrows<IllegalArgumentException> {
            TaskGenerator.generate(everydayTaskSpec.copy(title = ""))
        }
    }

    @Tag("negative")
    @Test
    fun `generate should throw DateTimeParseException when startTime is invalid date format`() {
        assertThrows<DateTimeParseException> {
            TaskGenerator.generate(everydayTaskSpec.copy(startTime = "1970-01-"))
        }
    }
}
