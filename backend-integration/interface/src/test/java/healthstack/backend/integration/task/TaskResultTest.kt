package healthstack.backend.integration.task

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Task Result Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskResultTest {
    @Test
    @Tag("positive")
    fun `task result get item result test`() {
        val res = TaskResult(
            "userId",
            "taskId",
            0,
            "startedAt",
            "submittedAt",
            listOf(
                ItemResult(
                    "itemName",
                    "result"
                )
            )
        )

        Assertions.assertEquals(res.itemResults[0].result, "result")
    }
}
