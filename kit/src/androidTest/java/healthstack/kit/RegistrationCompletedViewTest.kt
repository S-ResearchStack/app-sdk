package healthstack.kit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.signup.model.RegistrationCompletedModel
import healthstack.kit.task.signup.view.RegistrationCompletedView
import org.junit.Rule
import org.junit.Test

class RegistrationCompletedViewTest {
    @get:Rule
    val rule = createComposeRule()

    private fun createRegistrationCompletedModel(
        id: String = "registration-completed-model",
        title: String = "You are done!",
        buttonText: String = "Continue",
        description: String =
            "Congratulations! Everything is all set for you. " +
                "Now please tap on the button below to start your SleepCare journey!",
        drawableId: Int = R.drawable.sample_image_alpha4,
    ): RegistrationCompletedModel =
        RegistrationCompletedModel(id, title, buttonText, description, drawableId)

    private fun createRegistrationCompletedView(): RegistrationCompletedView =
        RegistrationCompletedView()

    @Test
    fun titleRenderSuccess() {
        val registrationCompletedModel = createRegistrationCompletedModel(title = "testTitle")
        val registrationCompletedView = createRegistrationCompletedView()
        val callbackCollection = CallbackCollection()

        rule.setContent {
            registrationCompletedView.Render(registrationCompletedModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()
    }
}
