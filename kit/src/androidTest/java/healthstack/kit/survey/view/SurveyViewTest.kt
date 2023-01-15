package healthstack.kit.survey.view

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.R
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.step.SurveyStep
import healthstack.kit.task.survey.view.SurveyView
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SurveyViewTest {

    @get:Rule
    val rule = createComposeRule() as AndroidComposeTestRule<*, *>
    private val questionnaire = listOf(
        ChoiceQuestionModel(
            "choice-question-model-1",
            "1. Are you between 30 and 50 years old?",
            "",
            candidates = listOf("Yes", "No", "Prefer not to answer"),
            answer = "Yes"
        ),
        ChoiceQuestionModel(
            "choice-question-model-2",
            "2. Do you have a family history of cardiovascular diseases?",
            "Examples include stroke, heart attack, high blood pressure, etc.",
            candidates = listOf("Yes", "No"),
            answer = "Yes"
        )
    )

    @Test
    fun testSinglePageSurveyView() {
        val view = SurveyView(pageable = false)
        val surveyStep = SurveyStep.Builder("survey-step", view)
            .addQuestions(questionnaire)
            .build()
        var submit = false

        rule.setContent {
            surveyStep.Render(
                callbackCollection = object : CallbackCollection() {
                    override fun next() {
                        submit = true
                    }
                }
            )
        }

        // Check if multiple questions are existed
        // TODO perform scroll action and submit
        questionnaire.forEach { q ->
            rule.onNodeWithText(q.question)
                .assertExists()
        }

        rule.onNodeWithText(rule.activity.getString(R.string.submit))
            .performClick()

        assertTrue(submit)
    }
}
