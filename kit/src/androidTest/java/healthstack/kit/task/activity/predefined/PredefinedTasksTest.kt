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

class PredefinedTasksTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val mobileSpirometryActivityTask = MobileSpirometryActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Activity",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testMobileSpirometryActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                mobileSpirometryActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    private val sustainedPhonationActivityTask = SustainedPhonationActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Activity",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testSustainedPhonationActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                sustainedPhonationActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    private val reactionTimeActivityTask = ReactionTimeActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Activity",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testReactionTimeActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                reactionTimeActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    private val speechRecognitionActivityTask = SpeechRecognitionActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Activity",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testSpeechRecognitionActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                speechRecognitionActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }
}
