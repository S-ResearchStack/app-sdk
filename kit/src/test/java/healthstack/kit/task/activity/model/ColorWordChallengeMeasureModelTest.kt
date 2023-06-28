package healthstack.kit.task.activity.model

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@DisplayName("ColorWord Challenge Measure Model Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ColorWordChallengeMeasureModelTest {
    @Tag("positive")
    @Test
    fun `color word challenge measure model test`() {

        val model = ColorWordChallengeMeasureModel(
            "id"
        )

        val word = model.getRandomWord()

        assertTrue(model.colorWords.contains(word))
    }
}
