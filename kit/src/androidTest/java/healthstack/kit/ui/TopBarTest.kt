package healthstack.kit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.lightColors
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TopBarTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testButtonClick() {
        var backButtonClicked = false
        var actionButtonClicked = false
        val topBarTitle = "TopBar Test"
        rule.setContent {
            AppTheme(colors = lightColors()) {
                TopBar(
                    title = topBarTitle,
                    color = AppTheme.colors.onSurface,
                    onClickBack = { backButtonClicked = true },
                    onClickAction = { actionButtonClicked = true },
                    actionIcon = Icons.Default.MoreVert,
                )
            }
        }

        rule.onNodeWithText(topBarTitle)
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
        assertTrue(backButtonClicked)

        rule.onNodeWithContentDescription("action button icon")
            .assertExists()
            .performClick()
        assertTrue(actionButtonClicked)
    }
}
