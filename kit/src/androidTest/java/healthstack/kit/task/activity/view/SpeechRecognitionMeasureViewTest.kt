package healthstack.kit.task.activity.view

import android.speech.SpeechRecognizer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.sensor.SpeechRecognitionListener
import healthstack.kit.sensor.SpeechRecognitionManager
import healthstack.kit.task.activity.model.SpeechRecognitionMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Rule
import org.junit.Test

class SpeechRecognitionMeasureViewTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun speechRecognitionMeasureViewTest() {
        mockkObject(SpeechRecognitionManager)
        val listener = mockk<SpeechRecognitionListener>()
        every { SpeechRecognitionManager.listener } returns listener
        every { listener.currentAmplitude } returns 300

        val speechRecognizer = mockk<SpeechRecognizer>()
        every { SpeechRecognitionManager.speechRecognizer } returns speechRecognizer
        justRun { speechRecognizer.setRecognitionListener(listener) }
        justRun { speechRecognizer.startListening(any()) }

        rule.setContent {
            AppTheme(mainLightColors()) {
                val context = LocalContext.current
                SpeechRecognitionManager.initialize(context)

                val model = SpeechRecognitionMeasureModel("id")
                SpeechRecognitionMeasureView().Render(
                    model,
                    CallbackCollection(),
                    null
                )
            }
        }

        rule.onNodeWithText("Start Recording")
            .assertExists()
            .performClick()

        rule.waitUntil {
            rule.onAllNodesWithText("Recording")
                .fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithText("Jaded zombies acted quaintly but kept driving their oxen forward.")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
