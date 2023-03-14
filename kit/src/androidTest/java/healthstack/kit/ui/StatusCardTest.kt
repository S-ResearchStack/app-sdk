package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.R.drawable
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class StatusCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testStatusCard() {
        val value = "2"
        val unit = "BPM"
        rule.setContent {
            AppTheme(colors = mainLightColors()) {
                StatusCard(drawableId = drawable.ic_100tb, value = value, unit = unit)
            }
        }

        rule.onNodeWithText(value)
            .assertExists()

        rule.onNodeWithText(unit)
            .assertExists()
    }
}
