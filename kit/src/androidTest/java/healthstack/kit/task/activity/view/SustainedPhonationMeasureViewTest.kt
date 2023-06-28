package healthstack.kit.task.activity.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.sensor.AudioRecorder
import healthstack.kit.task.activity.model.SustainedPhonationMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import java.util.Timer
import kotlin.concurrent.schedule

class SustainedPhonationMeasureViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private fun ComposeContentTestRule.waitForTime(time: Long) {
        AsyncTimer.start(time)
        this.waitUntil(time + 1000L) {
            AsyncTimer.complete
        }
    }

    object AsyncTimer {
        var complete = false
        fun start(time: Long) {
            complete = false
            Timer().schedule(time) {
                complete = true
            }
        }
    }

    @Test
    fun sustainedPhonationMeasureViewTest() {
        val recorder = mockk<AudioRecorder.Companion>()
        justRun { recorder.startRecording(any()) }
        justRun { recorder.stopRecording() }
        justRun { recorder.discardRecording(any()) }
        every { recorder.getAmplitudes() } returns flowOf(20, -1, 30)

        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = SustainedPhonationMeasureModel(
                    id = "id",
                    recorder = recorder
                )

                SustainedPhonationMeasureView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Checking background noise level...")
            .assertIsDisplayed()

        rule.waitForTime(10000L)

        rule.onNodeWithContentDescription("breathing guide")
            .assertExists()
    }

    @Test
    fun soundLevelExceededTest() {
        val recorder = mockk<AudioRecorder.Companion>()
        justRun { recorder.startRecording(any()) }
        justRun { recorder.stopRecording() }
        justRun { recorder.discardRecording(any()) }
        every { recorder.getAmplitudes() } returns flowOf(3200)

        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = SustainedPhonationMeasureModel(
                    id = "id",
                    recorder = recorder
                )

                SustainedPhonationMeasureView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.waitUntil(1000L) {
            rule.onAllNodesWithText("Noise Level")
                .fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithText("Noise Level")
            .assertIsDisplayed()
    }

    @Test
    fun cancelSustainedPhonationMeasureViewTest() {
        val recorder = mockk<AudioRecorder.Companion>()
        justRun { recorder.startRecording(any()) }
        justRun { recorder.stopRecording() }
        justRun { recorder.discardRecording(any()) }
        every { recorder.getAmplitudes() } returns flowOf(3200)

        rule.setContent {
            AppTheme(mainLightColors()) {
                val model = SustainedPhonationMeasureModel(
                    id = "id",
                    recorder = recorder
                )

                SustainedPhonationMeasureView().Render(
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
