package com.samsung.healthcare.kit.view

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.SignUpModel
import org.junit.Rule
import org.junit.Test

class SignUpViewTest {
    @get:Rule
    val rule = createComposeRule()

    private fun createSignUpModel(
        id: String = "sign-up-model",
        title: String = "SleepCare",
        description: String = "Thanks for joining the study! Now please create an account to keep track of your data and keep it safe.",
        drawableId: Int = R.drawable.ic_sample_icon,
    ): SignUpModel =
        SignUpModel(id, title, description, drawableId)

    private fun createSignUpView(): SignUpView =
        SignUpView()

    @Test
    fun titleRenderSuccess() {
        val signUpModel = createSignUpModel(title = "testTitle")
        val signUpView = createSignUpView()
        val callbackCollection = CallbackCollection()

        rule.setContent {
            signUpView.Render(signUpModel, callbackCollection, null)
        }

        rule.onNodeWithText("testTitle").assertExists()
        rule.onNodeWithText("WrongTestTitle").assertDoesNotExist()
    }
}
