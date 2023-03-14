package healthstack.kit.ui

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.mainLightColors
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TaskCardTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testEnabledTaskButton() {
        var clicked = false
        val onClick = { clicked = true }
        rule.setContent {
            AppTheme(colors = mainLightColors()) {
                TaskCard(
                    taskName = "TaskName",
                    description = "Good For You",
                    onClick = onClick
                )
            }
        }

        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
            .performClick()

        assertTrue(clicked)
    }

    @Test
    fun testDisabledTaskButton() {
        rule.setContent {
            AppTheme(colors = mainLightColors()) {
                TaskCard(
                    taskName = "TaskName",
                    description = "Good For You",
                    isCompleted = true,
                )
            }
        }

        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertDoesNotExist()
    }
}
