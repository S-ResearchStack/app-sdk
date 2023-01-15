package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.blueColors
import healthstack.kit.theme.darkBlueColors
import org.junit.Rule
import org.junit.Test

class TextFieldTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testMaskTextFieldTest() {
        val text = "text message"
        rule.setContent {
            val colors = darkBlueColors()
            colors.updateColorsFrom(blueColors())
            AppTheme(colors = colors) {
                RoundTextField(
                    value = text,
                    onValueChange = { },
                    shouldMask = true
                )
            }
        }

        rule.onNodeWithText(text)
            .assertDoesNotExist()
    }

    @Test
    fun testUnmaskTextFieldTest() {
        val text = "text message"
        rule.setContent {
            AppTheme(colors = darkBlueColors()) {
                RoundTextField(
                    value = text,
                    onValueChange = { },
                )
            }
        }

        rule.onNodeWithText(text)
            .assertExists()
    }
}
