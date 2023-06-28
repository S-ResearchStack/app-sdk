package healthstack.kit.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class EducationCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun EducationCardTest() {
        rule.setContent {
            AppTheme(mainLightColors()) {

                EducationCard(
                    type = "type",
                    title = "title",
                    description = "description",
                    previewContent = @Composable {
                        Text(text = "hello world")
                    }
                )
            }
        }

        rule.onNodeWithText("title")
            .assertExists()

        rule.onNodeWithText("hello world")
            .assertExists()

        rule.onNodeWithText("description")
            .assertExists()

        rule.onNodeWithTag("educationCard")
            .assertExists()
            .performClick()
    }
}
