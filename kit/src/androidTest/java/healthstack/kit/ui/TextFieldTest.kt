package healthstack.kit.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TextFieldTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val testTag = "roundTextFieldTag"

    @Test
    fun testMaskTextFieldTest() {
        var text = "text message"
        rule.setContent {
            AppTheme(mainLightColors()) {
                RoundTextField(
                    modifier = Modifier.testTag(testTag),
                    value = text,
                    onValueChange = { text = it },
                    shouldMask = true
                )
            }
        }

        val node = rule.onNodeWithTag(testTag)
        val changed = "changedText"
        node.performTextReplacement(changed)
        node.assertTextEquals("••••••••••••")
        assertEquals(changed, text)
    }

    @Test
    fun testNotEnabledTextFieldTest() {
        val text = "text message"
        rule.setContent {
            AppTheme(mainLightColors()) {
                RoundTextBoxWithIcon(
                    label = "first",
                    modifier = Modifier.testTag(testTag),
                    value = text,
                    enabled = false
                )
            }
        }
        rule.onNodeWithText("first")
            .assertExists()
    }

    @Test
    fun testUnmaskTextFieldTest() {
        var text = "text message"
        rule.setContent {
            AppTheme(mainLightColors()) {
                RoundTextField(
                    modifier = Modifier.testTag(testTag),
                    value = text,
                    onValueChange = { text = it },
                )
            }
        }
        val node = rule.onNodeWithTag(testTag)
        val changed = "changed"
        node.performTextReplacement(changed)
        assertEquals(changed, text)
    }
}
