package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class CircularTimerTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun circularTimerTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                CircularTimer()
            }
        }

        rule.onNodeWithTag("timer")
            .assertExists()
    }
}
