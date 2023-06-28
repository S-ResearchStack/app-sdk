package healthstack.kit.task.activity.view.common

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.task.activity.model.common.SimpleAudioActivityModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test
import healthstack.kit.R
import healthstack.kit.task.base.CallbackCollection

class SimpleAudioActivityViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun simpleAudioActivityViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = SimpleAudioActivityModel(
                    "id",
                    "title",
                    "header",
                    listOf("body1", "body2"),
                    R.drawable.ic_activity_sustained_phonation,
                    "button"
                )

                SimpleAudioActivityView<SimpleAudioActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }
}
