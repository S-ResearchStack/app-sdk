package healthstack.kit.task.activity.predefined

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.activity.model.ReactionTimeMeasureModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockkConstructor
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ReactionTimeActivityTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>
    val id = "id"
    val name = "Reaction Time"
    val completionTitle = "completionTitle"
    val completionDescription = listOf("completionDescription")

    private val reactionTimeActivityTask = ReactionTimeActivityTask(
        id = "id",
        taskId = "taskId",
        name = name,
        description = "description",
        completionTitle = completionTitle,
        completionDescription = completionDescription,
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testReactionTimeActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                reactionTimeActivityTask.CardView { }
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    @Test
    fun testReactionTimeActivityTaskCancel() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                reactionTimeActivityTask.Render()
            }
        }
        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }

    @Test
    fun testReactionTimeActivityTaskFail() {
        mockkConstructor(ReactionTimeMeasureModel::class)
        every { anyConstructed<ReactionTimeMeasureModel>().title } returns "Reaction Time"
        every { anyConstructed<ReactionTimeMeasureModel>().goal } returns "square"
        justRun { anyConstructed<ReactionTimeMeasureModel>().init() }
        every { anyConstructed<ReactionTimeMeasureModel>().getData() } returns flowOf(true)
        every { anyConstructed<ReactionTimeMeasureModel>().isGoal(any()) } returns false

        rule.setContent {
            SensorUtils.initialize(LocalContext.current)
            AppTheme(mainLightColors()) {
                reactionTimeActivityTask.Render()
            }
        }
        rule.onNodeWithText("Begin")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Shake phone when a square appears.")
            .assertExists()

        rule.waitUntil(30000) {
            rule.onAllNodesWithContentDescription("reaction shape")
                .fetchSemanticsNodes().isNotEmpty()
        }

        rule.waitUntil(30000) {
            rule.onAllNodesWithText("Your attempt was not successful. Please try again.")
                .fetchSemanticsNodes().size == 1
        }

        rule.waitUntil(30000) {
            rule.onAllNodesWithTag("countdown")
                .fetchSemanticsNodes().size == 1
        }
    }
}
