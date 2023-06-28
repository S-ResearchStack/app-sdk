package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class InsightCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun insightCardWithProgressTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                InsightCardWithProgress(
                    title = "title",
                    current = 1000,
                    total = 2000,
                    unit = "sessions",
                )
            }
        }

        rule.onNodeWithText("50 %")
            .assertExists()

        rule.onNodeWithText("1,000 / 2,000 sessions")
            .assertExists()
    }

    @Test
    fun thinInsightCardTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ThinInsightCard(
                    title = "title",
                    insightUnit = InsightUnit(
                        value = "100",
                        unit = "steps"
                    )
                )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()

        rule.onNodeWithText("100 steps")
            .assertExists()
    }

    @Test
    fun insightCardTest() {
        val time = LocalDateTime.now()

        rule.setContent {
            AppTheme(mainLightColors()) {

                InsightCard(
                    title = "title",
                    lastUpdatedTime = time,
                    insightUnits = listOf(
                        InsightUnit(
                            value = "100",
                            unit = "steps"
                        )
                    )
                )
            }
        }

        val resultTime = time.format(
            DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.SHORT
            )
        )

        rule.onNodeWithText("Last Updated: $resultTime")
            .assertExists()

        rule.onNodeWithText("100")
            .assertExists()

        rule.onNodeWithText("steps")
            .assertExists()
    }
}
