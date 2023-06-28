package healthstack.kit.task.activity.predefined

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.task.activity.model.ColorWordChallengeIntroModel
import healthstack.kit.task.activity.model.ColorWordChallengeMeasureModel
import healthstack.kit.task.activity.model.ColorWordChallengeResultModel
import healthstack.kit.task.activity.step.ColorWordChallengeMeasureStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class ColorWordChallengeActivityTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val model = ColorWordChallengeMeasureModel("id", "Color Word Challenge", 1)

    private val colorWordChallengeActivityTask = ColorWordChallengeActivityTask(
        id = "id",
        taskId = "taskId",
        name = "Color Word Challenge",
        description = "description",
        completionTitle = "completionTitle",
        completionDescription = listOf("completionDescription"),
        steps = listOf(
            SimpleViewActivityStep(
                "id", "Color Word Challenge",
                ColorWordChallengeIntroModel("id", "Color Word Challenge")
            ),
            ColorWordChallengeMeasureStep(
                "id", "Color Word Challenge",
                model
            ),
            SimpleViewActivityStep(
                "id",
                "Color Word Challenge",
                ColorWordChallengeResultModel(
                    "id", "Color Word Challenge",
                    header = "completionTitle", body = listOf("completionDescription")
                )
            ),
        ),
        isCompleted = false,
        isActive = true
    )

    @Test
    fun testColorWordChallengeActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                colorWordChallengeActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    @Test
    fun testColorWordChallengeActivityTask() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                colorWordChallengeActivityTask.Render()
            }
        }

        rule.onNodeWithText("Begin")
            .assertExists()
            .performClick()

        val tag = model.colorWords[model.testset[0][model.testset[0][0]]]

        rule.onNodeWithTag(tag)
            .assertExists()
            .performClick()

        rule.onNodeWithText("Back to Home")
            .assertExists()
            .performClick()
    }
}
