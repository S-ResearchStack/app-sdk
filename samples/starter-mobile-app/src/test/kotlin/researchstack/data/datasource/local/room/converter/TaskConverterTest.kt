package researchstack.data.datasource.local.room.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.ActivityType.TAPPING_SPEED
import researchstack.domain.model.task.taskresult.ActivityResult
import java.time.LocalDateTime

internal class TaskConverterTest {
    private val converter = TaskConverter()

    @Test
    @Tag(POSITIVE_TEST)
    fun `jsonToTask should return original task`() {
        val localDateTime = LocalDateTimeConverter().let {
            it.stringToTime(it.timeToString(LocalDateTime.now()))
        }!!

        val task = ActivityTask(
            id = 1,
            taskId = "taskId",
            studyId = "studyId",
            title = "title",
            description = "description",
            createdAt = localDateTime,
            scheduledAt = localDateTime,
            validUntil = localDateTime,
            inClinic = true,
            activityResult = ActivityResult(
                id = 1,
                startedAt = localDateTime,
                finishedAt = localDateTime,
                activityType = TAPPING_SPEED,
                result = mapOf(),
            ),
            completionTitle = "completion message",
            completionDescription = "completion description",
            activityType = TAPPING_SPEED,
        )

        assertEquals(
            task,
            converter.jsonToTask(converter.taskToJson(task))
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `jsonToTaskResult should return original result`() {
        val activityResult = ActivityResult(
            id = 1,
            startedAt = LocalDateTime.now(),
            finishedAt = LocalDateTime.now(),
            activityType = TAPPING_SPEED,
            result = mapOf(),
        )

        assertEquals(
            activityResult,
            converter.jsonToTaskResult(converter.taskResultToJson(activityResult))
        )
    }
}
