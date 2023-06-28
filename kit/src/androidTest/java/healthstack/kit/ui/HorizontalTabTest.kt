package healthstack.kit.ui

import androidx.compose.material.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainDarkColors
import org.junit.Rule
import org.junit.Test

class HorizontalTabTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun reactionTimeMeasureViewTest() {
        rule.setContent {
            AppTheme(mainDarkColors()) {
                HorizontalTab(
                    listOf(
                        TabContent("title1") {
                            Text("body1")
                        },
                        TabContent("title2") {
                            Text("body2")
                        }
                    )
                )
            }
        }

        rule.onNodeWithText("body1")
            .assertExists()

        rule.onNodeWithText("title2")
            .assertExists()
            .performClick()

        rule.onNodeWithText("body2")
            .assertExists()
    }
}
