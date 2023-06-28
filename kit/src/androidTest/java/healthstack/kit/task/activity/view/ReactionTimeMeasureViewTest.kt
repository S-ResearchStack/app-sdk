package healthstack.kit.task.activity.view

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.activity.model.ReactionTimeMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainDarkColors
import org.junit.Rule
import org.junit.Test

class ReactionTimeMeasureViewTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun reactionTimeMeasureViewTest() {
        rule.setContent {
            AppTheme(mainDarkColors()) {
                SensorUtils.initialize(LocalContext.current)
                ReactionTimeMeasureView().Render(
                    ReactionTimeMeasureModel(
                        id = "id",
                        title = "reaction time",
                        goal = "square",
                    ),
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Shake phone when a square appears.")
            .assertExists()

        rule.waitUntil(30000) {
            rule.onAllNodesWithText("Your attempt was not successful. Please try again.")
                .fetchSemanticsNodes().size == 1
        }
    }

    @Test
    fun cancelReactionTimeMeasureViewTest() {
        rule.setContent {
            AppTheme(mainDarkColors()) {
                ReactionTimeMeasureView().Render(
                    ReactionTimeMeasureModel(
                        id = "id",
                        title = "reaction time",
                        goal = "square",
                    ),
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
