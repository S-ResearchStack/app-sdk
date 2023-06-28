package healthstack.kit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.onboarding.model.EligibilityResultModel
import healthstack.kit.task.onboarding.view.EligibilityResultView
import org.junit.Rule
import org.junit.Test

class EligibilityResultViewTest {
    @get:Rule
    val rule = createComposeRule()

    private val eligibilitySuccessMessage: ImageArticleModel = ImageArticleModel(
        "success-message",
        "Great! You’re in!",
        "Congratulations! You are eligible for the study.",
        drawableId = R.drawable.sample_image_alpha2
    )

    private val eligibilityFailMessage: ImageArticleModel = ImageArticleModel(
        "fail-message",
        "You are not eligible for the study.",
        "Check back later and stay tuned for more studies coming soon!",
        drawableId = R.drawable.sample_image_alpha3
    )

    private fun createEligibilityCheckerModel(
        id: String = "eligibility-result-model",
        title: String = "Eligibility-Result-Title",
        drawableId: Int = R.drawable.sample_image_alpha1,
        successModel: ImageArticleModel = eligibilitySuccessMessage,
        failModel: ImageArticleModel = eligibilityFailMessage,
    ): EligibilityResultModel =
        EligibilityResultModel(id, title, drawableId, successModel, failModel)

    private fun createEligibilityResultView(): EligibilityResultView =
        EligibilityResultView()

    @Test
    fun titleRenderSuccess() {
        val eligibilityResultModel = createEligibilityCheckerModel(title = "testTitle")
        val eligibilityResultView = createEligibilityResultView()
        val callbackCollection = CallbackCollection()

        rule.setContent {
            eligibilityResultView.Render(eligibilityResultModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
