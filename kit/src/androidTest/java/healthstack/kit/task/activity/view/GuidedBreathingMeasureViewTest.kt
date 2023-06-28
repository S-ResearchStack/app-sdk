package healthstack.kit.task.activity.view

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.task.activity.model.GuidedBreathingMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class GuidedBreathingMeasureViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    val model = GuidedBreathingMeasureModel(
        id = "id",
        numCycle = 1,
        inhaleSecond = 1,
        exhaleSecond = 1
    )

    val view = GuidedBreathingMeasureView()

    @Test
    fun guidedBreathingMeasureViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                view.Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Ready")
            .assertExists()

        rule.waitUntil(5000) {
            rule.onAllNodesWithText("Exhale").fetchSemanticsNodes().size == 1
        }
    }

    @Test
    fun cancelGuidedBreathingMeasureViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                view.Render(
                    model,
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
