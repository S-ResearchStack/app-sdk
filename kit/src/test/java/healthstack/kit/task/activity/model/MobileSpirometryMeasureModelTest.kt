package healthstack.kit.task.activity.model

import healthstack.kit.sensor.AudioRecorder.Companion
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Mobile Spirometry Measure Model Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MobileSpirometryMeasureModelTest {
    @Tag("positive")
    @Test
    fun `mobile spirometry measure model test`() {
        val recorder = mockk<Companion>()

        val model = MobileSpirometryMeasureModel(
            "id",
            recorder = recorder
        )

        model.filePath = "filePath"

        Assertions.assertEquals(model.filePath, "filePath")
        Assertions.assertEquals(model.recorder, recorder)
    }
}
