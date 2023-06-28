package healthstack.kit.info

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainDarkColors
import org.junit.Rule
import org.junit.Test

class StudyInfoViewTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun StudyInfoViewTest() {
        rule.setContent {
            AppTheme(mainDarkColors()) {
                StudyInfoView().Render()
            }
        }

        rule.onNodeWithText("Study Contacts")
            .assertExists()

        rule.onNodeWithText("Study Explanation")
            .assertExists()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
