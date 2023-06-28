package healthstack.kit.task.survey.question.component

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.performClick
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel
import org.junit.Rule
import org.junit.Test

class DateTimeQuestionComponentTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    @Test
    fun testTimePickerQuestion() {
        rule.setContent {
            DateTimeQuestionComponent().Render(
                DateTimeQuestionModel(
                    "id",
                    "query",
                    isTime = true,
                    isDate = false,
                    isRange = false
                ),
                CallbackCollection()
            )
        }

        rule.onAllNodesWithTag("timePicker").onFirst()
            .performClick()

        rule.onAllNodesWithTag("timePicker").onLast()
            .performClick()
    }
}
