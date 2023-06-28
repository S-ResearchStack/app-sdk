package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.R
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Rule
import org.junit.Test

class PdfViewCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testPdfViewCard() {
        rule.setContent {
            AppTheme(mainLightColors()) {
                PdfViewCard(
                    type = "type",
                    title = "title",
                    buttonText = "view pdf",
                    filePath = "",
                    drawableId = R.drawable.ic_task
                )
            }
        }

        rule.onNodeWithText("view pdf")
            .assertExists()

        rule.onNodeWithText("title")
            .assertExists()
    }
}
