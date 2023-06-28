package healthstack.kit.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import healthstack.kit.ui.TextType.BULLET
import healthstack.kit.ui.TextType.NUMBER
import healthstack.kit.ui.TextType.PARAGRAPH
import org.junit.Rule
import org.junit.Test

class ListedTextTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun numberListTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ListedText(
                    bodies = listOf("body1", "body2"),
                    type = NUMBER
                )
            }
        }

        rule.onNodeWithText("1.")
            .assertExists()

        rule.onNodeWithText("body1")
            .assertExists()

        rule.onNodeWithText("2.")
            .assertExists()

        rule.onNodeWithText("body2")
            .assertExists()
    }

    @Test
    fun bulletListTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ListedText(
                    bodies = listOf("body1", "body2"),
                    type = BULLET
                )
            }
        }

        rule.onAllNodesWithText("•")
            .assertCountEquals(2)

        rule.onNodeWithText("body1")
            .assertExists()

        rule.onNodeWithText("body2")
            .assertExists()
    }

    @Test
    fun paragraphListTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                ListedText(
                    bodies = listOf("body1", "body2"),
                    type = PARAGRAPH
                )
            }
        }

        rule.onNodeWithText("body1")
            .assertExists()

        rule.onNodeWithText("body2")
            .assertExists()
    }
}
