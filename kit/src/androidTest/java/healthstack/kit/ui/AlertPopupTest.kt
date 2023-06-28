package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class AlertPopupTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun alertPopupConfirmTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                AlertPopup(
                    initiateText = "click",
                    title = "title",
                    body = "body",
                    confirmText = "confirm",
                    dismissText = "dismiss"
                )
            }
        }

        rule.onNodeWithText("click")
            .assertExists()
            .performClick()

        rule.onNodeWithText("confirm")
            .assertExists()
            .performClick()
    }

    @Test
    fun alertPopupDismissTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                AlertPopup(
                    initiateText = "click",
                    title = "title",
                    body = "body",
                    confirmText = "confirm",
                    dismissText = "dismiss"
                )
            }
        }

        rule.onNodeWithText("click")
            .assertExists()
            .performClick()

        rule.onNodeWithText("dismiss")
            .assertExists()
            .performClick()
    }
}
