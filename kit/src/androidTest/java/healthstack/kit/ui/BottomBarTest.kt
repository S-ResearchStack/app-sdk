package healthstack.kit.ui

import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.lightColors
import healthstack.kit.ui.ButtonShape.ROUND
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BottomBarTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testEanbledBottomBar() {
        var clicked = false
        val text = "bottom"

        rule.setContent {
            AppTheme(colors = lightColors()) {
                BottomBar(text = text, shape = ROUND) {
                    clicked = true
                }
            }
        }
        rule.onNodeWithText(text)
            .assertExists()
            .performClick()

        assertTrue(clicked)
    }

    @Test
    fun testDisabledBottomBar() {
        val text = "bottom"

        rule.setContent {
            AppTheme(colors = lightColors()) {
                BottomBar(text = text, buttonEnabled = false, shape = ROUND) { }
            }
        }
        rule.onNodeWithText(text)
            .assertExists()
            .assertIsNotEnabled()
    }

    @Test
    fun testEanbledBottomBarWithGradientBackground() {
        var clicked = false
        val text = "bottom"

        rule.setContent {
            AppTheme(colors = lightColors()) {
                BottomBarWithGradientBackground(text = text, shape = ROUND) {
                    clicked = true
                }
            }
        }
        rule.onNodeWithText(text)
            .assertExists()
            .performClick()

        assertTrue(clicked)
    }
}
