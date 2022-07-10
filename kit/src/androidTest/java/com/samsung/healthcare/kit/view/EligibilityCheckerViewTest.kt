package com.samsung.healthcare.kit.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.EligibilityCheckerModel
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel
import com.samsung.healthcare.kit.step.sub.QuestionSubStep
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.view.component.ChoiceQuestionComponent
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
        drawableId: Int = R.drawable.sample_image1,
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
