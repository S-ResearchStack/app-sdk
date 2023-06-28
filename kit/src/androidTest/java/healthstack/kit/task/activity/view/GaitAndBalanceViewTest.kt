package healthstack.kit.task.activity.view

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.activity.model.GaitAndBalanceBMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceGMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceIntroModel
import healthstack.kit.task.activity.model.GaitAndBalanceResultModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class GaitAndBalanceViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun gaitAndBalanceIntroViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = GaitAndBalanceIntroModel(
                    id = "id",
                    header = "title",
                )

                GaitAndBalanceIntroView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun gaitAndBalanceGMeasureViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = GaitAndBalanceGMeasureModel(
                    id = "id",
                    header = "title",
                    buttonText = "Done"
                )

                GaitAndBalanceGMeasureView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Done")
            .assertExists()
    }

    @Test
    fun gaitAndBalanceBMeasureViewTest() {
        rule.setContent {
            SensorUtils.initialize(LocalContext.current)
            AppTheme(mainLightColors()) {
                val model = GaitAndBalanceBMeasureModel(
                    id = "id",
                    header = "title",
                    timeSeconds = 1
                )

                GaitAndBalanceBMeasureView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()
    }

    @Test
    fun gaitAndBalanceResultViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = GaitAndBalanceResultModel(
                    id = "id",
                    header = "title"
                )

                GaitAndBalanceResultView().Render(
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
