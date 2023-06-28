package healthstack.kit.task.activity.predefined

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class GuidedBreathingActivityTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    val guidedBreathingActivityTask = GuidedBreathingActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Guided Breathing",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testGuidedBreathingActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                guidedBreathingActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    @Test
    fun testGuidedBreathingActivityTask() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                guidedBreathingActivityTask.Render()
            }
        }

        rule.onNodeWithText("Begin")
            .assertExists()
            .performClick()

        guidedBreathingActivityTask.pageCallbacks.next()

        rule.onNodeWithText("Back to Home")
            .assertExists()
    }
}
