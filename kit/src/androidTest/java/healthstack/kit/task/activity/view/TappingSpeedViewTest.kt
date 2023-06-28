package healthstack.kit.task.activity.view

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.task.activity.model.TappingSpeedIntroModel
import healthstack.kit.task.activity.model.TappingSpeedMeasureModel
import healthstack.kit.task.activity.model.TappingSpeedResultModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class TappingSpeedViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun tappingSpeedIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = TappingSpeedIntroModel(
                    id = "id",
                    buttonText = "Begin"
                )

                TappingSpeedIntroView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Begin")
            .assertExists()
    }

    @Test
    fun cancelTappingSpeedMeasureViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = TappingSpeedMeasureModel(
                    id = "id",
                    title = "title"
                )

                TappingSpeedMeasureView().Render(
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

    @Test
    fun tappingSpeedResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = TappingSpeedResultModel(
                    id = "id",
                    buttonText = "Back to Home"
                )

                TappingSpeedResultView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Back to Home")
            .assertExists()
    }
}
