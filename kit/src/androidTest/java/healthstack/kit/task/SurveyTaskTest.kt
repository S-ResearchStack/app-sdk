package healthstack.kit.task

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import healthstack.kit.R
import healthstack.kit.task.survey.SurveyTask
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel
import healthstack.kit.task.survey.question.model.TextInputQuestionModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.theme.darkBlueColors
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SurveyTaskTest {
    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>

    private val multiChoiceQuestionModel = MultiChoiceQuestionModel(
        id = "q-1",
        query = "choice multiple answer",
        explanation = "this is a sample for MultiChoiceQuestionModel",
        drawableId = null,
        answer = null,
        candidates = listOf("a", "b", "c")
    )

    private val textInputQuestionModel = TextInputQuestionModel(
        id = "q-2",
        query = "input some text",
        explanation = "sample for text input",
        drawableId = R.drawable.ic_task,
        answer = "answer",
    )

    private val surveyTask = SurveyTask.Builder(
        id = "task-1",
        revisionId = 1,
        taskId = "survey-sample",
        name = "sample survey",
        description = "test",
        callback = {},
    ).apply {
        addQuestion(multiChoiceQuestionModel)
        addQuestion(textInputQuestionModel)
    }.build()

    @Test
    fun testSurveyTaskCardView() {
        surveyTask.isCompleted = false
        rule.setContent {
            AppTheme(darkBlueColors()) {
                surveyTask.CardView {
                }
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertExists()
    }

    @Test
    fun testCompletedSurveyTaskCardView() {
        surveyTask.isCompleted = true
        rule.setContent {
            AppTheme(darkBlueColors()) {
                surveyTask.CardView {
                }
            }
        }
        rule.onNodeWithText(rule.activity.getString(R.string.start_task))
            .assertDoesNotExist()
    }

    @Test
    fun testSurveyTask() {
        surveyTask.isCompleted = false
        var completed = false
        surveyTask.callback = { completed = true }

        val step = surveyTask.step
        assertNotNull(step.id)
        assertNotNull(step.name)

        val state = step.getState()
        assertNotNull(state.id)
        assertNotNull(state.title)

        rule.setContent {
            AppTheme(darkBlueColors()) {
                surveyTask.Render()
            }
        }

        rule.onNodeWithTag(multiChoiceQuestionModel.candidates.random())
            .performClick()

        getNextButton(rule.activity.getString(R.string.next))
            .assertExists()
            .performClick()

        val textAnswer = "answer"
        rule.onNodeWithTag("TextQuestionInputField")
            .performTextInput(textAnswer)

        getNextButton(rule.activity.getString(R.string.complete))
            .assertExists()
            .performClick()

        assertTrue(completed)
        assertTrue(step.result)

        assertEquals(textAnswer, textInputQuestionModel.input)
    }

    private fun getNextButton(buttonName: String) =
        rule.onNodeWithText(buttonName)
}
