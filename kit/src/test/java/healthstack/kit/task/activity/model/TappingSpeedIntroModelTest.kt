package healthstack.kit.task.activity.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("Tapping Speed Intro Model Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TappingSpeedIntroModelTest {
    @Tag("positive")
    @Test
    fun `tapping speed intro model test`() {

        val model = TappingSpeedIntroModel(
            "id"
        )

        assertEquals(model.handType, "right")
    }
}
