package healthstack.kit.task.activity.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import healthstack.kit.task.activity.model.ColorWordChallengeMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class ColorWordChallengeViewTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun cancelColorWordChallengeMeasureViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ColorWordChallengeMeasureView().Render(
                    ColorWordChallengeMeasureModel(
                        id = "id",
                        numTest = 1
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
