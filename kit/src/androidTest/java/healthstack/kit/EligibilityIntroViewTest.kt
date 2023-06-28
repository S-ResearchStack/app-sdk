package healthstack.kit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.onboarding.model.EligibilityIntroModel
import healthstack.kit.task.onboarding.view.EligibilityIntroView
import org.junit.Rule
import org.junit.Test

class EligibilityIntroViewTest {
    @get:Rule
    val rule = createComposeRule()

    private val eligibilitySections: List<EligibilityIntroModel.EligibilityCondition> = listOf(
        EligibilityIntroModel.EligibilityCondition(
            "Medical eligibility",
            listOf("Condition(s)", "Prescription(s)", "Living in Europe")
        )
    )

    private fun createEligibilityIntroModel(
        id: String = "eligibility-intro-model",
        title: String = "Eligibility-Intro-Title",
        description: String = "description",
        conditions: List<EligibilityIntroModel.EligibilityCondition> = eligibilitySections,
        drawableId: Int = R.drawable.sample_image_alpha1,
        viewType: EligibilityIntroModel.ViewType = EligibilityIntroModel.ViewType.Card,
    ): EligibilityIntroModel = EligibilityIntroModel(
        id, title, description, drawableId, conditions, viewType
    )

    private fun createEligibilityIntroView(): EligibilityIntroView =
        EligibilityIntroView()

    @Test
    fun titleRenderSuccess() {
        val eligibilityIntroModel = createEligibilityIntroModel(title = "testTitle")
        val eligibilityIntroView = createEligibilityIntroView()
        val callbackCollection = CallbackCollection()

        rule.setContent {
            eligibilityIntroView.Render(eligibilityIntroModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
