package healthstack.kit.task.activity.predefined

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class TappingSpeedActivityTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val tappingSpeedActivityTask = TappingSpeedActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Tapping Speed",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testTappingSpeedActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                tappingSpeedActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    @Test
    fun testTappingSpeedActivityTaskCancel() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                tappingSpeedActivityTask.Render()
            }
        }

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }

    @Test
    fun testTappingSpeedActivityTask() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                tappingSpeedActivityTask.Render()
            }
        }

        rule.onNodeWithText("Begin")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Start Exercise")
            .assertExists()
            .performClick()

        rule.onAllNodesWithContentDescription("tapping icon")[0]
            .assertExists()
            .performClick()

        rule.waitUntil(11000) {
            tappingSpeedActivityTask.result.containsKey("right")
        }

        rule.onNodeWithText("Start Exercise")
            .assertExists()
            .performClick()

        rule.onAllNodesWithContentDescription("tapping icon")[0]
            .assertExists()
            .performClick()

        rule.waitUntil(11000) {
            tappingSpeedActivityTask.result.containsKey("left")
        }

        rule.onNodeWithText("Back to Home")
            .assertExists()
    }
}
