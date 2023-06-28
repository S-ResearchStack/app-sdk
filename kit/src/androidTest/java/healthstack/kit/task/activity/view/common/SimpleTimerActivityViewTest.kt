package healthstack.kit.task.activity.view.common

import android.Manifest
import android.hardware.SensorManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.activity.model.GaitAndBalanceBMeasureModel
import healthstack.kit.task.activity.model.RangeOfMotionMeasureModel
import healthstack.kit.task.activity.model.common.SimpleTimerActivityModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleTimerActivityViewTest {
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule
        .grant(Manifest.permission.VIBRATE)

    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun simpleActivityViewTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val context = LocalContext.current
                val sensorManager = mockk<SensorManager>()
                SensorUtils.initialize(context)
                SensorUtils.sensorManager = sensorManager

                val model = SimpleTimerActivityModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    dataPrefix = "prefix",
                    sensors = emptyList()
                )
                every { model.close() } returns Unit

                SimpleTimerActivityView<SimpleTimerActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()

        rule.onNodeWithText("body1")
            .assertExists()
    }

    @Test
    fun simpleActivityViewWithGaitAndBalanceTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val context = LocalContext.current
                val sensorManager = mockk<SensorManager>()
                SensorUtils.initialize(context)
                SensorUtils.sensorManager = sensorManager

                val model = GaitAndBalanceBMeasureModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    dataPrefix = "prefix",
                    sensors = emptyList(),
                    timeSeconds = 5
                )
                every { model.close() } returns Unit

                SimpleTimerActivityView<SimpleTimerActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()

        rule.onNodeWithText("body1")
            .assertExists()
    }

    @Test
    fun simpleActivityViewWithRangeOfMotionTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                val context = LocalContext.current
                val sensorManager = mockk<SensorManager>()
                SensorUtils.initialize(context)
                SensorUtils.sensorManager = sensorManager

                val model = RangeOfMotionMeasureModel(
                    id = "id",
                    title = "title",
                    header = "header",
                    body = listOf("body1", "body2"),
                    dataPrefix = "prefix",
                    sensors = emptyList(),
                    timeSeconds = 5
                )
                every { model.close() } returns Unit

                SimpleTimerActivityView<SimpleTimerActivityModel>()
                    .Render(
                        model,
                        CallbackCollection(),
                        null
                    )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()

        rule.onNodeWithText("body1")
            .assertExists()
    }
}
