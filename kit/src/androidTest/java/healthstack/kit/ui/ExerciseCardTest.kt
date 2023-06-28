package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class ExerciseCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun exerciseCardTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ExerciseCard(
                    "Today's workout",
                    listOf("Treadmill", "Cycle")
                )
            }
        }

        rule.onNodeWithText("Today's workout")
            .assertExists()

        rule.onNodeWithText("Treadmill")
            .assertExists()

        rule.onNodeWithText("Cycle")
            .assertExists()
    }
}
