package healthstack.kit.info

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.notification.AlarmUtils
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainDarkColors
import org.junit.Rule
import org.junit.Test

class SettingsViewTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun settingsViewTest() {
        rule.setContent {
            AppTheme(mainDarkColors()) {
                val context = LocalContext.current
                AlarmUtils.initialize(context)

                SettingsView().Render()
            }
        }

        rule.onNodeWithText("Settings")
            .assertExists()

        rule.onNodeWithText("Reminders")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
