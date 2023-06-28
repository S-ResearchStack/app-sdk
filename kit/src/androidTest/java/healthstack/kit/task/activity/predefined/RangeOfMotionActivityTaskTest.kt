package healthstack.kit.task.activity.predefined

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.activity.model.GaitAndBalanceBMeasureModel
import healthstack.kit.task.activity.model.RangeOfMotionIntroModel
import healthstack.kit.task.activity.model.RangeOfMotionMeasureModel
import healthstack.kit.task.activity.model.RangeOfMotionResultModel
import healthstack.kit.task.activity.step.RangeOfMotionMeasureStep
import healthstack.kit.task.activity.step.common.SimpleTimerActivityStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import healthstack.kit.ui.util.InteractionType.VIBRATE
import org.junit.Rule
import org.junit.Test

class RangeOfMotionActivityTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>
    val id = "id"
    val name = "Range Of Motion"
    val completionTitle = "completionTitle"
    val completionDescription = listOf("completionDescription")

    private val rangeOfMotionActivityTask = RangeOfMotionActivityTask(
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
                RangeOfMotionIntroModel(id, name)
            ),
            SimpleViewActivityStep(
                id, name,
                RangeOfMotionIntroModel(
                    id = id,
                    title = name,
                    header = "Right Arm Circumduction",
                    body = listOf(
                        "Place phone in your right hand.",
                        "Straighten your right arm and move it in a full circle for 20 sec."
                    ),
                    drawableId = R.drawable.ic_activity_range_of_motion_right_arm,
                    buttonText = "Start Exercise",
                )
            ),
            RangeOfMotionMeasureStep(
                id, name, RangeOfMotionMeasureModel(id, name, timeSeconds = 1),
            ),
            SimpleViewActivityStep(
                id, name,
                RangeOfMotionResultModel(
                    id,
                    name,
                    header = completionTitle,
                    body = completionDescription,
                    buttonText = "Continue"
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
                id,
                name,
                RangeOfMotionIntroModel(
                    id = id,
                    title = name,
                    header = "Left Arm Circumduction",
                    body = listOf(
                        "Place phone in your left hand.",
                        "Straighten your left arm and move it in a full circle for 20 sec.",
                    ),
                    drawableId = R.drawable.ic_activity_range_of_motion_left_arm,
                    buttonText = "Start Exercise",
                )
            ),
            RangeOfMotionMeasureStep(
                id,
                name,
                RangeOfMotionMeasureModel(
                    id,
                    name,
                    header = "Left Arm Circumduction",
                    body = listOf(
                        "Place phone in your left hand.",
                        "Straighten your left arm and move it in a full circle for 20 sec",
                    ),
                    isRightHand = false,
                    timeSeconds = 1
                )
            ),
            SimpleViewActivityStep(
                id,
                name,
                RangeOfMotionResultModel(
                    id,
                    name,
                    header = completionTitle,
                    body = completionDescription,
                )
            ),
        ),
    )

    @Test
    fun testRangeOfMotionActivityTaskCardView() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                rangeOfMotionActivityTask.CardView {}
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()
    }

    @Test
    fun testRangeOfMotionActivityTask() {
        rule.setContent {
            SensorUtils.initialize(LocalContext.current)
            AppTheme(mainLightColors()) {
                rangeOfMotionActivityTask.Render()
            }
        }

        rule.onNodeWithText("Begin")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Start Exercise")
            .assertExists()
            .performClick()

        rule.waitUntil(2000) {
            rangeOfMotionActivityTask.result.containsKey("right_times(ms)")
        }

        rule.onNodeWithText("Continue")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Start Exercise")
            .assertExists()
            .performClick()

        rule.waitUntil(2000) {
            rangeOfMotionActivityTask.result.containsKey("left_times(ms)")
        }

        rule.onNodeWithText("Back to Home")
            .assertExists()
    }
}
