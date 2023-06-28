package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class ReminderSwitchTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun reminderSwitchTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ToggleSwitch(
                    "toggle",
                    false
                )
            }
        }

        rule.onNodeWithText("toggle")
            .assertExists()

        rule.onNodeWithTag("toggle")
            .assertExists()
            .performClick()
    }
}
