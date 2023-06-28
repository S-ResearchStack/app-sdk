package healthstack.kit

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.firebase.auth.FirebaseAuth
import healthstack.kit.auth.SignInProvider.Basic
import healthstack.kit.auth.SignInProvider.Google
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.signup.model.SignUpModel
import healthstack.kit.task.signup.view.SignUpView
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Rule
import org.junit.Test

class SignUpViewTest {
    @get:Rule
    val rule = createComposeRule()

    private fun createSignUpModel(
        id: String = "sign-up-model",
        title: String = "SleepCare",
        description: String =
            "Thanks for joining the study! Now please create an account to keep track of your data and keep it safe.",
        drawableId: Int = R.drawable.ic_home_task,
    ): SignUpModel =
        SignUpModel(id, title, listOf(Basic, Google), description, drawableId)

    private fun createSignUpView(): SignUpView =
        SignUpView()

    @Test
    fun titleRenderSuccess() {
        val signUpModel = createSignUpModel(title = "testTitle")
        val signUpView = createSignUpView()
        val callbackCollection = CallbackCollection()

        mockkStatic(FirebaseAuth::class)
        val firebaseAuth = mockk<FirebaseAuth>()
        every { FirebaseAuth.getInstance() } returns firebaseAuth

        rule.setContent {
            signUpView.Render(signUpModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()

        rule.onNodeWithContentDescription("back button icon")
            .assertExists()
            .performClick()
    }
}
