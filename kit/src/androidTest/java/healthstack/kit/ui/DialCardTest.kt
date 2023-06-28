package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class DialCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun dialCardTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                DialCard(
                    R.drawable.ic_call,
                    "jiyun",
                    "head researcher",
                    "012-345-6789"
                )
            }
        }

        rule.onNodeWithText("jiyun")
            .assertExists()

        rule.onNodeWithText("head researcher")
            .assertExists()

        rule.onNodeWithTag("dial")
            .assertExists()
            .performClick()
    }
}
