package healthstack.kit.task.activity.view

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.sensor.AudioRecorder
import healthstack.kit.task.activity.model.MobileSpirometryMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MobileSpirometryMeasureViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun mobileSpirometryMeasureViewTest() {
        val recorder = mockk<AudioRecorder.Companion>()
        justRun { recorder.startRecording(any()) }
        justRun { recorder.stopRecording() }
        justRun { recorder.discardRecording(any()) }
        every { recorder.getAmplitudes() } returns flowOf(20, -1, 30)

        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = MobileSpirometryMeasureModel(
                    id = "id",
                    recorder = recorder
                )

                MobileSpirometryMeasureView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Breathe Forcefully 3 Times")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()

        rule.onNodeWithText("Stop Recording")
            .assertExists()
            .performClick()
    }
}
