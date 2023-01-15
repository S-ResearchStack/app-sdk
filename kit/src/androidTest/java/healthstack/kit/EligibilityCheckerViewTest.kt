package healthstack.kit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.onboarding.model.EligibilityCheckerModel
import healthstack.kit.task.onboarding.view.EligibilityCheckerView
import healthstack.kit.task.survey.question.QuestionSubStep
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.task.survey.question.component.ChoiceQuestionComponent
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import org.junit.Rule
import org.junit.Test

class EligibilityCheckerViewTest {
    @get:Rule
    val rule = createComposeRule()

    private val questionnaireSubSteps: List<QuestionSubStep<*, *>> = listOf(
        QuestionSubStep(
            "question-1",
            "Question-Name-1",
            ChoiceQuestionModel(
                "choice-question-model-1",
                "Do you have any existing cardiac conditions?",
                "Examples of cardiac conditions include abnormal heart rhythms, or arrhythmias",
                candidates = listOf("Yes", "No"),
                answer = "Yes"
            ),
            ChoiceQuestionComponent(),
        )
    )

    private fun createEligibilityCheckerModel(
        id: String = "eligibility-checker-model",
        title: String = "Eligibility-Checker-Title",
        drawableId: Int = R.drawable.sample_image_alpha1,
    ): EligibilityCheckerModel =
        EligibilityCheckerModel(id, title, drawableId)

    private fun createEligibilityCheckerView(
        pageable: Boolean = true,
    ): EligibilityCheckerView = EligibilityCheckerView(pageable)

    @Test
    fun titleRenderSuccess() {
        val eligibilityCheckerModel = createEligibilityCheckerModel(title = "testTitle")
        val eligibilityCheckerView = createEligibilityCheckerView(true)
        val callbackCollection = CallbackCollection()

        rule.setContent {
            eligibilityCheckerView.Render(
                eligibilityCheckerModel,
                callbackCollection,
                SubStepHolder(
                    "sub-step-holder",
                    "Sub-Step-Holder",
                    questionnaireSubSteps
                )
            )
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()
    }
}
