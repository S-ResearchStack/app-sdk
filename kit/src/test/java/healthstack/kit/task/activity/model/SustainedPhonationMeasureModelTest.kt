package healthstack.kit.task.activity.model

import healthstack.kit.sensor.AudioRecorder
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Sustained Phonation Measure Model Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SustainedPhonationMeasureModelTest {
    @Tag("positive")
    @Test
    fun `sustained phonation measure model test`() {
        val recorder = mockk<AudioRecorder.Companion>()
        justRun { recorder.stopRecording() }

        val model = SustainedPhonationMeasureModel(
            "id",
            recorder = recorder
        )

        model.filePath = "filePath"

        assertEquals(model.filePath, "filePath")
        assertEquals(model.recorder, recorder)

        model.stop()
        verify {
            recorder.stopRecording()
        }
    }
}
