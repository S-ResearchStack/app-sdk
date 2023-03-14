package healthstack.kit.task.task.signup.view

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import healthstack.kit.task.signup.view.BasicSignUpComponent
import healthstack.kit.task.signup.view.EMAIL_TEST_TAG
import healthstack.kit.task.signup.view.PASSWORD_CONFIRM_TEST_TAG
import healthstack.kit.task.signup.view.PASSWORD_TEST_TAG
import org.junit.Rule
import org.junit.Test

class BasicSignUpComponentTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun passwordBasedSignUpComponentTest() {
        rule.setContent {
            BasicSignUpComponent { }
        }

        val email = "test@research-test.com"
        rule.onNodeWithTag(EMAIL_TEST_TAG).performTextInput("test@research-test.com")
        rule.onNodeWithTag(EMAIL_TEST_TAG).assertTextEquals(email)

        val pw = "guessWhat!@#"
        val masked = "••••••••••••"
        rule.onNodeWithTag(PASSWORD_TEST_TAG).performTextInput(pw)
        rule.onNodeWithTag(PASSWORD_CONFIRM_TEST_TAG).performTextInput(pw)
        rule.onNodeWithTag(PASSWORD_TEST_TAG).assertTextEquals(masked)
    }
}
