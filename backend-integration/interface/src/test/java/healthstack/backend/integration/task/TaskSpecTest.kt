package healthstack.backend.integration.task

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Task Spec Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskSpecTest {
    @Test
    @Tag("positive")
    fun `all properties test`() {
        val textProperties = TextProperties(
            tag = "text",
        )

        assertEquals(textProperties.tag, "text")

        val rankingProperties = RankingProperties(
            tag = "rank",
            options = listOf(Option("rank1"), Option("rank2"), Option("rank3"))
        )

        assertEquals(rankingProperties.tag, "rank")
        assertEquals(rankingProperties.options.size, 3)

        val dateTimeProperties = DateTimeProperties(
            tag = "datetime",
            isTime = false,
            isDate = true,
            isRange = false
        )

        assertEquals(dateTimeProperties.tag, "datetime")
        assertEquals(dateTimeProperties.isTime, false)
        assertEquals(dateTimeProperties.isDate, true)
        assertEquals(dateTimeProperties.isRange, false)

        val skipLogic = SkipLogic(
            "condition",
            1
        )

        assertEquals(skipLogic.condition, "condition")
        assertEquals(skipLogic.goToItemSequence, 1)

        val contents = Contents(
            "tag",
            true,
            completionTitle = "title",
            completionDescription = "description"
        )

        assertEquals(contents.completionTitle, "title")
        assertEquals(contents.completionDescription, "description")

        val scaleProperties = ScaleProperties(
            "tag",
            low = 0,
            high = 10,
            lowLabel = "low",
            highLabel = "high"
        )

        assertEquals(scaleProperties.lowLabel, "low")
        assertEquals(scaleProperties.highLabel, "high")
    }
}
