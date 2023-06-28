package healthstack.kit.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class BottomBarNavigationTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val items = listOf(
        BottomNavItem(
            title = "first",
            icon = Icons.Default.Home
        )
    )

    @Test
    fun testBottomBarNavigation() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                BottomBarNavigation(items = items)
            }
        }

        rule.onNodeWithContentDescription("first_icon")
            .assertExists()
    }
}
