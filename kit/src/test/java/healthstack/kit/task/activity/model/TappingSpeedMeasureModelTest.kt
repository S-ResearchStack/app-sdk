package healthstack.kit.task.activity.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Tapping Speed Measure Model Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TappingSpeedMeasureModelTest {
    @Tag("positive")
    @Test
    fun `tapping speed measure model test`() {

        val model = TappingSpeedMeasureModel(
            "id",
            "title",
            measureTimeSecond = 5
        )

        assertEquals(model.measureTimeSecond, 5)
    }
}
