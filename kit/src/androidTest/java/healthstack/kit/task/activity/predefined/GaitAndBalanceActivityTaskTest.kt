package healthstack.kit.task.activity.predefined

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.R.drawable
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.activity.model.GaitAndBalanceBMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceGMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceIntroModel
import healthstack.kit.task.activity.model.GaitAndBalanceResultModel
import healthstack.kit.task.activity.step.common.SimpleTimerActivityStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import healthstack.kit.ui.util.InteractionType.VIBRATE
import org.junit.Rule
import org.junit.Test

class GaitAndBalanceActivityTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>
    val id = "id"
    val name = "Gait and Balance"
    val completionTitle = "completionTitle"
    val completionDescription = listOf("completionDescription")

    private val gaitAndBalanceActivityTask = GaitAndBalanceActivityTask(
        id = id,
        taskId = "taskId",
        name = name,
        description = "description",
        completionTitle = completionTitle,
        completionDescription = completionDescription,
        isCompleted = false,
        isActive = true,
        steps = listOf(
            SimpleViewActivityStep(
                id, name,
                GaitAndBalanceIntroModel(id)
            ),
            SimpleViewActivityStep(
                id, name,
                GaitAndBalanceGMeasureModel(
                    id,
                    header = "Walk in a straight line unassisted for 20 steps",
                    buttonText = "Done"
                )
            ),
            SimpleViewActivityStep(
                id, name,
                GaitAndBalanceGMeasureModel(
                    id,
                    header = "Turn around and walk back to your starting point",
                    drawableId = drawable.ic_activity_gait_and_balance_back,
                    buttonText = "Done"
                )
            ),
            SimpleTimerActivityStep(
                id, name,
                GaitAndBalanceBMeasureModel(
                    id,
                    header = "Stand still for 20 seconds",
                    timeSeconds = 1,
                    interactionType = VIBRATE,
                )
            ),
            SimpleViewActivityStep(
                id, name,
                GaitAndBalanceResultModel(id, name, header = completionTitle, body = completionDescription)
            )
        ),
    )

    @Test
    fun testGaitAndBalanceActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                gaitAndBalanceActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    @Test
    fun testGaitAndBalanceActivityTask() {
        rule.setContent {
            SensorUtils.initialize(LocalContext.current)
            AppTheme(mainLightColors()) {
                gaitAndBalanceActivityTask.Render()
            }
        }

        rule.onNodeWithText("Begin")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Done")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Done")
            .assertExists()
            .performClick()

        rule.waitUntil(2000) {
            gaitAndBalanceActivityTask.result.containsKey("gait_balance_times(ms)")
        }

        rule.onNodeWithText("Back to Home")
            .assertExists()
    }
}
