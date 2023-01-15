package healthstack.kit.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.darkColors
import java.time.LocalDate
import org.junit.Rule
import org.junit.Test

class WeeklyCardTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testWeeklyCard() {
        rule.setContent {
            AppTheme(colors = darkColors()) {
                WeeklyCard(LocalDate.of(2015, 7, 7))
            }
        }
        // 7, July 2015 is Tuesday
        rule.onNodeWithText("4").assertDoesNotExist()
        for (date in 5..11) {
            rule.onNodeWithText(date.toString()).assertExists()
        }
        rule.onNodeWithText("12").assertDoesNotExist()
    }
}
