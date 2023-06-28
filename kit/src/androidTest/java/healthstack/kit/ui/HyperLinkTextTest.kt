package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import healthstack.kit.ui.HyperLinkAction.DIAL
import healthstack.kit.ui.HyperLinkAction.EMAIL
import org.junit.Rule
import org.junit.Test

class HyperLinkTextTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun hyperLinkTextTestWeb() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                HyperLinkText("web", "https://s-healthstack.io")
            }
        }

        rule.onNodeWithText("web")
            .assertExists()
            .performClick()
    }

    @Test
    fun hyperLinkTextTestDial() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                HyperLinkText("dial", "010-1234-5678", DIAL)
            }
        }

        rule.onNodeWithText("dial")
            .assertExists()
            .performClick()
    }

    @Test
    fun hyperLinkTextTestEmail() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                HyperLinkText("email", "jjyun.do@samsung.com", EMAIL)
            }
        }

        rule.onNodeWithText("email")
            .assertExists()
            .performClick()
    }
}
